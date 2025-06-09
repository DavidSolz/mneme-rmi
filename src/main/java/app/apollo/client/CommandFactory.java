package app.apollo.client;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CommandFactory is responsible for creating command objects based on user input.
 * It uses a map to associate command names with their respective classes.
 */
public class CommandFactory {
    private SessionManager sessionManager; // Manages session-related operations.
    private Map<String, Class<? extends Command>> classMap; // Maps command names to their classes.
    
    /**
     * Constructor to initialize the CommandFactory with a SessionManager.
     * It also populates the classMap with available commands.
     * 
     * @param sessionManager The session manager to use for managing user sessions.
     */
    public CommandFactory(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        classMap = new HashMap<>();
        classMap.put("ListFiles", ListFilesCommand.class); // Command to list files.
        classMap.put("Logout", LogoutCommand.class);       // Command to log out.
        classMap.put("Upload", UploadCommand.class);       // Command to upload a file.
        classMap.put("Download", DownloadCommand.class);   // Command to download a file.
        classMap.put("Delete", DeleteCommand.class);       // Command to delete a file.
        classMap.put("Login", LoginCommand.class);         // Command to log in.
        classMap.put("Register", RegisterCommand.class);   // Command to register a new user.
    }
    
    /**
     * Creates a command object based on the user input and user ID.
     * It validates the input, retrieves the corresponding command class,
     * and initializes the command with the necessary context.
     * 
     * @param input The user input specifying the command and its parameters.
     * @param userID The ID of the user executing the command.
     * @return A Command object if the input is valid, null otherwise.
     */
    public Command createCommand(String input, String userID) {
        Command command = null;
        SessionContext sessionContext;
        List<String> parameters = Arrays.asList(input.split(" ")); // Split input into parameters.
        
        if (checkUserInput(parameters)) { // Validate the user input.
            try {
                // Retrieve the command class and create a new instance.
                command = classMap.get(parameters.get(0)).getConstructor().newInstance();
                
                // Load the session context for the user.
                sessionContext = sessionManager.loadFromFile(userID);
                if (sessionContext == null) {
                    sessionContext = new SessionContext();
                    sessionContext.setToken("Invalid Token"); // Set a default invalid token.
                }
                
                // Set the context and parameters for the command.
                command.setEverything(parameters, sessionContext, sessionManager);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException |
                     InvocationTargetException | NoSuchMethodException | SecurityException e) {
                // Handle exceptions and print error messages.
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Błędna instrukcja"); // Print an error message for invalid input.
        }
        
        return command; // Return the created command or null if input was invalid.
    }
    
    /**
     * Validates the user input to ensure it matches the expected format for commands.
     * 
     * @param parameters The list of input parameters.
     * @return True if the input is valid, false otherwise.
     */
    private boolean checkUserInput(List<String> parameters) {
        if (parameters.size() == 0) { // Input must not be empty.
            return false;
        }
        
        // Validate input based on the command name and expected parameter count.
        if (parameters.get(0).equals("ListFiles")) {
            if (parameters.size() != 1) {
                return false;
            }
        } else if (parameters.get(0).equals("Logout")) {
            if (parameters.size() != 1) {
                return false;
            }
        } else if (parameters.get(0).equals("Upload")) {
            if (parameters.size() != 2) {
                return false;
            }
        } else if (parameters.get(0).equals("Download")) {
            if (parameters.size() != 3) {
                return false;
            }
        } else if (parameters.get(0).equals("Delete")) {
            if (parameters.size() != 2) {
                return false;
            }
        } else if (parameters.get(0).equals("Login")) {
            if (parameters.size() != 3) {
                return false;
            }
        } else if (parameters.get(0).equals("Register")) {
            if (parameters.size() != 3) {
                return false;
            }
        } else {
            return false; // Command name is not recognized.
        }
        
        return true; // Input is valid.
    }
}
