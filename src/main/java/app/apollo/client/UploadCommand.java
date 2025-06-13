package app.apollo.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.security.auth.login.CredentialException;

import app.apollo.common.Block;
import app.apollo.common.CrypticEngine;
import app.apollo.common.FrozenPair;

public class UploadCommand implements Command {

    private final Context ctx;

    public UploadCommand(Context ctx) {
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

    private List<String> findFilenamesByRegex(String regex) {

        File baseDir;
        String fileRegex;

        int lastSlash = Math.max(regex.lastIndexOf('/'), regex.lastIndexOf('\\'));

        if (lastSlash >= 0) {
            String dirPath = regex.substring(0, lastSlash);
            baseDir = new File(dirPath);
            fileRegex = regex.substring(lastSlash + 1);
        } else {
            baseDir = new File(".");
            fileRegex = regex;
        }

        if (!baseDir.exists() || !baseDir.isDirectory()) {
            throw new IllegalArgumentException("Invalid directory in regex path: " + baseDir.getPath());
        }

        Pattern pattern = Pattern.compile(fileRegex);

        return Stream.of(baseDir.listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .filter(name -> pattern.matcher(name).matches())
                .map(name -> new File(baseDir, name).getPath())
                .collect(Collectors.toList());

    }

    public void upload(String filepath) throws Exception {

        int blockSize = (int) ctx.fileService.getBlockSize();
        File inputFile = new File(filepath);

        if (!inputFile.exists()) {
            throw new FileNotFoundException("File `" + filepath + "` does not exist or cannot be opened.");
        }

        if (inputFile.length() == 0) {
            System.out.println("Warning: File " + filepath + " is empty, skipping upload.");
            return;
        }

        System.out.println("\n=== Uploading file: " + inputFile.getName() + " ===");

        long numBlocks = (inputFile.length() + blockSize - 1) / blockSize;
        System.out.println("Estimated number of blocks: " + numBlocks);

        ctx.fileService.setFileBlockCount(ctx.session.getToken(), inputFile.getName(), numBlocks);
        System.out.println("Current base block size: " + blockSize);

        List<FrozenPair<String, String>> checksums = ctx.fileService.getChecksums(ctx.session.getToken(),
                inputFile.getName());

        if (checksums == null) {
            checksums = Collections.emptyList();
        }

        long startTime = System.currentTimeMillis();

        try (FileInputStream stream = new FileInputStream(inputFile)) {

            int blockNum = 0;

            byte[] buffer = new byte[blockSize];

            while (true) {
                int totalRead = 0;

                while (totalRead < blockSize) {
                    int read = stream.read(buffer, totalRead, blockSize - totalRead);
                    if (read == -1)
                        break;
                    totalRead += read;
                }

                if (totalRead == 0) {
                    break;
                }

                byte[] blockData = Arrays.copyOf(buffer, blockSize);

                if (totalRead < blockSize) {
                    Arrays.fill(blockData, totalRead, blockSize, (byte) 0);
                }

                String fingerprint = CrypticEngine.weakHash(blockData, blockSize);
                String checksum = CrypticEngine.strongHash(fingerprint, blockData, blockSize);

                Block block = new Block();
                block.setData(blockData);
                block.setSize(totalRead);
                block.setFingerprint(fingerprint);
                block.setChecksum(checksum);
                block.setSequenceNumber((long) blockNum);
                block.setUserId(ctx.session.getUserId());

                System.out.println("Block size: " + totalRead);

                boolean needsUpload = true;

                if (blockNum < checksums.size()) {
                    FrozenPair<String, String> remoteChecksum = checksums.get(blockNum);

                    if (fingerprint.equals(remoteChecksum.getFirst()) && checksum.equals(remoteChecksum.getSecond())) {
                        needsUpload = false;
                    }
                }

                if (needsUpload) {
                    try {
                        ctx.fileService.uploadBlock(ctx.session.getToken(), inputFile.getName(), block);
                    } catch (Exception e) {
                        System.err.printf("Error uploading block %d: %s%n", blockNum, e.getMessage());
                        throw e;
                    }
                }

                blockNum += 1;

                double percent = (blockNum * 100.0) / numBlocks;
                long elapsed = System.currentTimeMillis() - startTime;
                long remaining = (long) ((elapsed / (double) blockNum) * (numBlocks - blockNum));

                printProgress("Uploading", percent, remaining);
            }

        }

        System.out.println();
    }

    @Override
    public void execute(String[] args) throws Exception {

        if (args.length < 2) {
            throw new IllegalArgumentException("Usage: upload <regular expression>");
        }

        if (ctx.session.getToken() == null || ctx.session.getToken().isEmpty()) {
            throw new CredentialException("Access failed: No active session.");
        }

        List<String> matches = findFilenamesByRegex(args[1]);

        for (String match : matches) {
            try {
                upload(match);
            } catch (Exception e) {
                System.err.println("Upload failed for " + match + ": " + e.getMessage());
            }
        }

    }

    @Override
    public String getName() {
        return "upload";
    }

    @Override
    public String getDescription() {
        return "upload <regular expression> - Uploads a set of files that matches expression onto server";
    }

}
