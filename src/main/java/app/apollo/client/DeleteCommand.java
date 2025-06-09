package app.apollo.client;

import java.util.List;

/**
 * DeleteCommand is responsible for handling the file deletion process.
 * It validates the session token and deletes the specified file from the remote service.
 */
public class DeleteCommand implements Command {

    private String fileName; // The name of the file to delete.
    private SessionContext sessionContext; // Holds the session context for the current user.

    /**
     * Default constructor for DeleteCommand.
     */
    public DeleteCommand() {}
    
    /**
     * Executes the command to delete a file.
     * It validates the session token and deletes the file if the user is authenticated.
     * 
     * @param fileClient The FileClient instance for file-related operations.
     * @param authClient The AuthClient instance for authentication operations.
     */
    @Override
    public void execute(FileClient fileClient, AuthClient authClient) {
        // Check if the session token is valid.
        if (authClient.validateToken(sessionContext.getToken())) {
            // Delete the file using the FileClient.
            fileClient.delete(fileName, sessionContext.getToken());
        } else {
            // Print a message if the user is not logged in.
            System.out.println("Nie jesteś zalogowany");
        }
    }

    /**
     * Sets the necessary context and parameters for the command.
     * 
     * @param parameters     The list of input parameters (file name).
     * @param sessionContext The session context for the current user.
     * @param sessionManager The session manager for managing session data (not used here).
     */
    @Override
    public void setEverything(List<String> parameters, SessionContext sessionContext, SessionManager sessionManager) {
        // Extract the file name from the input parameters.
        this.fileName = parameters.get(1);
        this.sessionContext = sessionContext; // Set the session context.
    }
}
