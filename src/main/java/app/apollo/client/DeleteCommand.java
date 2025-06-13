package app.apollo.client;

import java.io.FileNotFoundException;
import java.util.List;

import javax.security.auth.login.CredentialException;

public class DeleteCommand implements Command {

    private final Context ctx;

    public DeleteCommand(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void execute(String[] args) throws Exception {

        if (args.length < 2) {
            throw new IllegalArgumentException("Usage: delete <filename>");
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

        try {
            ctx.fileService.deleteFile(token, filename);
            System.out.println("File `" + filename + "` deleted successfully.");
        } catch (Exception e) {
            throw new Exception("Failed to delete file `" + filename + "`.", e);
        }

    }

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String getDescription() {
        return "delete <filename> - Deletes a file from the server";
    }

}
