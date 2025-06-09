package app.apollo.client;

import java.util.List;

/**
 * UploadCommand is responsible for handling the file upload process.
 * It validates the session token and uploads the specified file to the remote service.
 */
public class UploadCommand implements Command {

    private String localPath; // The local path of the file to upload.
    private SessionContext sessionContext; // Holds the session context for the current user.
    
    /**
     * Default constructor for UploadCommand.
     */
    public UploadCommand() {}
    /**
     * Executes the command to upload a file.
     * It validates the session token and uploads the file if the user is authenticated.
     * 
     * @param fileClient The FileClient instance for file-related operations.
     * @param authClient The AuthClient instance for authentication operations.
     */
    @Override
    public void execute(FileClient fileClient, AuthClient authClient) {
        // Check if the session token is valid.
        if (authClient.validateToken(sessionContext.getToken())) {
            // Upload the file using the FileClient.
            fileClient.upload(localPath, sessionContext.getToken());
        } else {
            // Print a message if the user is not logged in.
            System.out.println("Nie jesteś zalogowany");
        }
    }

    /**
     * Sets the necessary context and parameters for the command.
     * 
     * @param parameters     The list of input parameters (local path of the file).
     * @param sessionContext The session context for the current user.
     * @param sessionManager The session manager for managing session data (not used here).
     */
    @Override
    public void setEverything(List<String> parameters, SessionContext sessionContext, SessionManager sessionManager) {
        // Extract the local path of the file from the input parameters.
        this.localPath = parameters.get(1);
        this.sessionContext = sessionContext; // Set the session context.
    }
}
