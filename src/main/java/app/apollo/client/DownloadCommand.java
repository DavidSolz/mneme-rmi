package app.apollo.client;

import java.util.List;

/**
 * DownloadCommand is responsible for handling the file download process.
 * It validates the session token and downloads the specified file from the remote service.
 */
public class DownloadCommand implements Command {
    private String localPath; // The local path where the file will be saved.
    private String fileName; // The name of the file to download.
    private SessionContext sessionContext; // Holds the session context for the current user.

    /**
     * Executes the command to download a file.
     * It validates the session token and downloads the file if the user is authenticated.
     * 
     * @param fileClient The FileClient instance for file-related operations.
     * @param authClient The AuthClient instance for authentication operations.
     */
    @Override
    public void execute(FileClient fileClient, AuthClient authClient) {
        // Check if the session token is valid.
        if (authClient.validateToken(sessionContext.getToken())) {
            // Download the file using the FileClient.
            fileClient.download(fileName, localPath, sessionContext.getToken());
        } else {
            // Print a message if the user is not logged in.
            System.out.println("Nie jesteś zalogowany");
        }
    }

    /**
     * Sets the necessary context and parameters for the command.
     * 
     * @param parameters     The list of input parameters (local path and file name).
     * @param sessionContext The session context for the current user.
     * @param sessionManager The session manager for managing session data (not used here).
     */
    @Override
    public void setEverything(List<String> parameters, SessionContext sessionContext, SessionManager sessionManager) {
        // Extract the local path and file name from the input parameters.
        this.localPath = parameters.get(1);
        this.fileName = parameters.get(2);
        this.sessionContext = sessionContext; // Set the session context.
    }
}
