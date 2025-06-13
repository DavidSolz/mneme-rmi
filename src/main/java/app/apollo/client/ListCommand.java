package app.apollo.client;

import java.io.FileNotFoundException;
import java.util.List;

import javax.security.auth.login.CredentialException;

public class ListCommand implements Command {

    private final Context ctx;

    public ListCommand(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void execute(String[] args) throws Exception {

        String token = ctx.session.getToken();
        if (token == null || token.isEmpty()) {
            throw new CredentialException("Access denied: No active session.");
        }

        List<String> remoteFiles = ctx.fileService.listFiles(token);

        if(remoteFiles.isEmpty())
        {
            System.out.println("Remote storage does not contain any file.");
            return;
        }

        System.out.println("\n=== Remote storage ===");
        for(String filename : remoteFiles)
        {
            System.out.println("- " + filename);
        }

        System.out.println();

    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "list - Fetches a list of the remote storage files";
    }

}
