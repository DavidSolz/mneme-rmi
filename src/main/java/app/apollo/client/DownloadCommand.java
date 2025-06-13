package app.apollo.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import javax.security.auth.login.CredentialException;

import app.apollo.common.Block;

public class DownloadCommand implements Command {

    private final Context ctx;

    public DownloadCommand(Context ctx) {
        this.ctx = ctx;
    }

    private void printProgress(String operation, double percent, long remainingMillis) {
        int width = 30;
        int pos = (int) (percent / 100.0 * width);
        StringBuilder bar = new StringBuilder();
        bar.append("\r").append(operation).append(" [");

        for (int i = 0; i < width; ++i) {
            if (i < pos)
                bar.append("=");
            else if (i == pos)
                bar.append(">");
            else
                bar.append(" ");
        }

        bar.append("] ");
        bar.append(String.format("%.1f", percent)).append("%");

        long seconds = remainingMillis / 1000;
        bar.append(" - ETA: ").append(seconds).append("s");

        System.out.print(bar);
    }

    @Override
    public void execute(String[] args) throws Exception {

        if (args.length < 2) {
            throw new IllegalArgumentException("Usage: download <filename>");
        }

        String token = ctx.session.getToken();
        if (token == null || token.isEmpty()) {
            throw new CredentialException("Access denied: No active session.");
        }

        List<String> remoteFiles = ctx.fileService.listFiles(token);

        String filename = args[1].trim();

        if (filename.isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be empty.");
        }

        boolean exists = remoteFiles.stream().anyMatch(x -> x.equals(filename));

        if (!exists) {
            throw new FileNotFoundException(String.format("File `%s` does not exist on remote storage.", filename));
        }

        long totalBlocks = ctx.fileService.getFileBlockCount(token, filename);
        String outputPath = Paths.get(".", filename).toAbsolutePath().toString();

        File localFile = new File(filename);

        if (!localFile.exists()) {
            localFile.createNewFile();
        }

        long startTime = System.currentTimeMillis();

        try (FileOutputStream out = new FileOutputStream(outputPath)) {
            for (long i = 0; i < totalBlocks; i++) {

                Block block = ctx.fileService.downloadBlock(token, filename, i);

                if (block != null) {

                    byte[] blockData = block.getData();

                    if (blockData == null) {
                        System.err.println("Warning: Block data is null for block " + i);
                        continue;
                    }

                    int dataSize = block.getSize();

                    if (dataSize > blockData.length) {
                        System.err.println("Warning: Declared block size exceeds actual data size. Adjusting.");
                        dataSize = blockData.length;
                    }

                    out.write(blockData, 0, dataSize);
                } else {
                    System.err.println("Warning: Block " + i + " is empty or null.");
                }

                Long blockNum = block.getSequenceNumber();

                double percent = (blockNum * 100.0) / totalBlocks;
                long elapsed = System.currentTimeMillis() - startTime;
                long remaining = (long) ((elapsed / (double) blockNum) * (totalBlocks - blockNum));

                printProgress("Downloading", percent, remaining);
            }
        } catch (IOException e) {
            throw new IOException("Failed to write file `" + filename + "` to disk.", e);
        }

    }

    @Override
    public String getName() {
        return "download";
    }

    @Override
    public String getDescription() {
        return "download <filename> - Downloads a file from the server";
    }

}
