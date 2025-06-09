package app.apollo.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * SessionManager is responsible for managing session data by saving and loading
 * session tokens to and from files.
 */
public class SessionManager {
    private String basePath; // Base directory for storing session files.

    /**
     * Constructor to initialize the SessionManager with a base path.
     * 
     * @param basePath The base directory for session files.
     */
    public SessionManager(String basePath) {
        this.basePath = basePath;
    }

    /**
     * Loads a session context from a file.
     * 
     * @param fileName The name of the file to load the session from.
     * @return A SessionContext object if the file exists, null otherwise.
     */
    public SessionContext loadFromFile(String fileName) {
        SessionContext sessionContext = null; // Initialize the session context as null.
        Path filePath = Paths.get(basePath + fileName); // Construct the file path.
        String token = ""; // Initialize the token as an empty string.

        try {
            // Check if the file exists.
            if (Files.exists(filePath)) {
                // Read the token from the file.
                token = new String(Files.readAllBytes(filePath), StandardCharsets.US_ASCII);
                sessionContext = new SessionContext(); // Create a new SessionContext object.
                sessionContext.setToken(token); // Set the token in the session context.
            }
        } catch (IOException e) {
            // Handle file read errors and print the error message.
            System.out.println(e.getMessage());
        }

        return sessionContext; // Return the session context or null if the file does not exist.
    }

    /**
     * Saves a session token to a file.
     * 
     * @param fileName The name of the file to save the session to.
     * @param token    The session token to save.
     */
    public void saveToFile(String fileName, String token) {
        Path filePath = Paths.get(basePath + fileName); // Construct the file path.
        byte[] data = token.getBytes(); // Convert the token to a byte array.

        try {
            // Check if the base directory exists, and create it if it does not.
            if (!Files.exists(Paths.get(basePath))) {
                Files.createDirectories(Paths.get(basePath));
            }

            // Check if the file exists, and create it if it does not.
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

            // Write the token data to the file.
            Files.write(filePath, data);
        } catch (IOException e) {
            // Handle file write errors and print the error message.
            System.out.println(e.getMessage());
        }
    }
}
