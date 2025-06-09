package app.apollo.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * LogoutCommand is responsible for handling the logout process.
 * It invalidates the user's session token and updates the session file.
 */
public class LogoutCommand implements Command {

    private SessionContext sessionContext; // Holds the session context for the current user.
    private static String basePath; // Base directory for storing session-related files.

    /**
     * Default constructor for LogoutCommand.
    */
    public LogoutCommand() {}
    /**
     * Sets the base path for session-related files.
     * 
     * @param basePath The base directory to set.
     */
    public static void setBasePath(String basePath) {
        LogoutCommand.basePath = basePath;
    }

    /**
     * Executes the logout process.
     * It validates the session token, logs the user out, and invalidates the session file.
     * 
     * @param fileClient The FileClient instance for file-related operations.
     * @param authClient The AuthClient instance for authentication operations.
     */
    @Override
    public void execute(FileClient fileClient, AuthClient authClient) {
        // Check if the session token is valid.
        if (authClient.validateToken(sessionContext.getToken())) {
            // Log the user out using the session token.
            authClient.logout(sessionContext.getToken());

            // Retrieve the user's ID and construct the path to the session file.
            String userID = String.valueOf(fileClient.getClientID());
            Path filename = Paths.get(basePath, userID);

            // Check if the session file exists.
            if (Files.exists(filename)) {
                try {
                    // Write "Invalid Token" to the session file to invalidate it.
                    Files.write(filename, "Invalid Token".getBytes());
                } catch (IOException e) {
                    // Handle file write errors and print the error message.
                    System.out.println(e.getMessage());
                }
            }
        } else {
            // Print a message if the user is not logged in.
            System.out.println("Nie jesteś zalogowany");
        }
    }

    /**
     * Sets the necessary context and parameters for the command.
     * 
     * @param parameters     The list of input parameters.
     * @param sessionContext The session context for the current user.
     * @param sessionManager The session manager for managing session data.
     */
    @Override
    public void setEverything(List<String> parameters, SessionContext sessionContext, SessionManager sessionManager) {
        this.sessionContext = sessionContext; // Set the session context.
    }
}
