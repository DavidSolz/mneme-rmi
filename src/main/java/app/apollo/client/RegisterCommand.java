package app.apollo.client;

import java.util.List;

/**
 * RegisterCommand is responsible for handling the user registration process.
 * It interacts with the AuthClient to register a new user with the provided credentials.
 */
public class RegisterCommand implements Command {
    private String userName; // The username provided by the user.
    private String password; // The password provided by the user.

    /**
     * Executes the registration process.
     * It sends the username and password to the AuthClient for registration.
     * 
     * @param fileClient The FileClient instance for file-related operations (not used here).
     * @param authClient The AuthClient instance for authentication operations.
     */
    @Override
    public void execute(FileClient fileClient, AuthClient authClient) {
        // Attempt to register the user with the provided credentials.
        if (authClient.register(userName, password)) {
            // Print a success message if registration is successful.
            System.out.println("Rejestracja zakończona powodzeniem");
        } else {
            // Print an error message if registration fails.
            System.out.println("Rejestracja zakończona niepowodzeniem");
        }
    }

    /**
     * Sets the necessary context and parameters for the command.
     * 
     * @param parameters     The list of input parameters (username and password).
     * @param sessionContext The session context for the current user (not used here).
     * @param sessionManager The session manager for managing session data (not used here).
     */
    @Override
    public void setEverything(List<String> parameters, SessionContext sessionContext, SessionManager sessionManager) {
        // Extract the username and password from the input parameters.
        this.userName = parameters.get(1);
        this.password = parameters.get(2);
    }
}
