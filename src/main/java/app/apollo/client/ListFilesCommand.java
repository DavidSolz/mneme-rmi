package app.apollo.client;

import java.util.List;

/**
 * ListFilesCommand is responsible for listing all files available on the remote service.
 * It validates the session token and retrieves the list of files if the user is authenticated.
 */
public class ListFilesCommand implements Command {

    private SessionContext sessionContext; // Holds the session context for the current user.

    /**
     * Executes the command to list files.
     * It validates the session token and retrieves the list of files from the remote service.
     * 
     * @param fileClient The FileClient instance for file-related operations.
     * @param authClient The AuthClient instance for authentication operations.
     */
    @Override
    public void execute(FileClient fileClient, AuthClient authClient) {
        // Check if the session token is valid.
        if (authClient.validateToken(sessionContext.getToken())) {
            List<String> fileList; // List to store the retrieved file names.

            // Retrieve the list of files from the remote service.
            fileList = fileClient.listFiles(sessionContext.getToken());

            // Print the list of files.
            System.out.println("Files list:");
            for (String i : fileList) {
                System.out.println("- " + i);
            }
        } else {
            // Print a message if the user is not logged in.
            System.out.println("Nie jesteś zalogowany");
        }
    }

    /**
     * Sets the necessary context and parameters for the command.
     * 
     * @param parameters     The list of input parameters (not used here).
     * @param sessionContext The session context for the current user.
     * @param sessionManager The session manager for managing session data (not used here).
     */
    @Override
    public void setEverything(List<String> parameters, SessionContext sessionContext, SessionManager sessionManager) {
        this.sessionContext = sessionContext; // Set the session context.
    }
}
