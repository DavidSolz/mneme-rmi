package app.apollo.client;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.security.auth.login.CredentialException;

import app.apollo.common.Block;

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

    public void upload(String filepath) throws Exception{
        
        int blockSize = (int) ctx.fileService.getBlockSize();

        File inputFile = new File(filepath);

        long numBlocks = (long) Math.ceil(inputFile.length()/blockSize);

        ctx.fileService.setFileBlockCount(ctx.session.getToken(), filepath, numBlocks);

        FileInputStream stream = new FileInputStream(inputFile);
        
        int bytesRead = 0;
        int offset = 0;

        byte[] data = new byte[blockSize];
        while( (bytesRead = stream.read(data, offset, blockSize)) != -1 )
        {
            Block block = new Block();
            ctx.fileService.uploadBlock(ctx.session.getToken(), filepath, block);
            offset += bytesRead;
        }

        stream.close();

    }

    @Override
    public void execute(String[] args) throws Exception {

        if (args.length < 2) {
            throw new IllegalArgumentException("Usage: upload <regular expression>");
        }

        if(ctx.session.getToken() == null || ctx.session.getToken().isEmpty())
        {
            throw new CredentialException("Access failed: No active session.");
        }

        List<String> matches = findFilenamesByRegex(args[1]);

        for (String match : matches) {
            upload(match);
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
