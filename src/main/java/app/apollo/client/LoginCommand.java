package app.apollo.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import app.apollo.common.Session;

/**
 * LoginCommand is responsible for handling the login process.
 * It validates user credentials, retrieves a session token, and saves session data.
 */
public class LoginCommand implements Command {
    private SessionManager sessionManager; // Manages session-related operations.
    private String username; // The username provided by the user.
    private String password; // The password provided by the user.
    private static String lastLoggedUserFileName; // File to store the last logged user's ID.

    /**
     * Sets the file name used to store the last logged user's ID.
     * 
     * @param fileName The file name to set.
     */
    public static void setLastLoggedUserFileName(String fileName) {
        LoginCommand.lastLoggedUserFileName = fileName;
    }

    /**
     * Executes the login process.
     * It validates the user's credentials, retrieves a session token, and saves session data.
     * 
     * @param fileClient The FileClient instance for file-related operations.
     * @param authClient The AuthClient instance for authentication operations.
     */
    @Override
    public void execute(FileClient fileClient, AuthClient authClient) {
        int userId; // Variable to store the user's ID.
        Session session = authClient.login(username, password); // Attempt to log in with the provided credentials.

        if (session != null) { // Check if the login was successful.
            String token = session.getToken(); // Retrieve the session token.
            if (authClient.validateToken(token)) { // Validate the session token.
                System.out.println("Zalogowano"); // Print a success message.
                userId = session.getUserId(); // Retrieve the user's ID from the session.
                fileClient.setClientID(userId); // Set the user's ID in the FileClient.
                sessionManager.saveToFile(String.valueOf(userId), token); // Save the session token to a file.

                try {
                    // Check if the file for storing the last logged user's ID exists, and create it if it does not.
                    if (!Files.exists(Paths.get(lastLoggedUserFileName))) {
                        Files.createFile(Paths.get(lastLoggedUserFileName));
                    }
                    // Write the user's ID to the file.
                    Files.write(Paths.get(lastLoggedUserFileName), String.valueOf(userId).getBytes());
                } catch (IOException e) {
                    // Handle file write errors and print the error message.
                    System.out.println(e.getMessage());
                }
            } else {
                // Print an error message if the token validation fails.
                System.out.println("Logowanie nieudane");
            }
        } else {
            // Print an error message if the login fails.
            System.out.println("Logowanie nieudane");
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
        this.sessionManager = sessionManager; // Set the session manager.
        this.username = parameters.get(1); // Extract the username from the parameters.
        this.password = parameters.get(2); // Extract the password from the parameters.
    }
}
