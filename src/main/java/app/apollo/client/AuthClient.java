package app.apollo.client;

import java.rmi.RemoteException;

import app.apollo.common.AuthService;
import app.apollo.common.Session;

/**
 * AuthClient is a client-side class that interacts with the remote AuthService.
 * It provides methods for user authentication, registration, and session management.
 */
public class AuthClient {
    private AuthService authService; // Reference to the remote authentication service.
    
    /**
     * Constructor to initialize the AuthClient with a remote AuthService.
     * @param authService The remote authentication service.
     */
    public AuthClient(AuthService authService){
        this.authService = authService;
    }
    
    /**
     * Logs in a user with the provided username and password.
     * @param username The username of the user.
     * @param password The password of the user.
     * @return A Session object if login is successful, null otherwise.
     */
    public Session login(String username, String password){
        try {
            return authService.login(username, password); // Calls the remote login method.
        } catch (RemoteException e) {
            // Handles RemoteException and prints the error message.
            System.out.println(e.getMessage());
        }
        return null; // Returns null if an exception occurs.
    }
    
    /**
     * Registers a new user with the provided username and password.
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @return True if registration is successful, false otherwise.
     */
    public boolean register(String username, String password){
        try {
            return authService.register(username, password); // Calls the remote register method.
        } catch (RemoteException e) {
            // Handles RemoteException and prints the error message.
            System.out.println(e.getMessage());
        }
        return false; // Returns false if an exception occurs.
    }
    
    /**
     * Logs out a user using the provided token.
     * @param token The session token of the user.
     */
    public void logout(String token){
        try {
            authService.logout(token); // Calls the remote logout method.
        } catch (RemoteException e) {
            // Handles RemoteException and prints the error message.
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Validates a session token.
     * @param token The session token to validate.
     * @return True if the token is valid, false otherwise.
     */
    public boolean validateToken(String token){
        try {
            return authService.validateToken(token); // Calls the remote validateToken method.
        } catch (RemoteException e) {
            // Handles RemoteException and prints the error message.
            System.out.println(e.getMessage());
        }
        return false; // Returns false if an exception occurs.
    }
    
    /**
     * Logs in a user using a session token.
     * @param token The session token of the user.
     * @return A Session object if login is successful, null otherwise.
     */
    public Session login(String token){
        try {
            return authService.login(token); // Calls the remote login method with a token.
        } catch (RemoteException e) {
            // Handles RemoteException and prints the error message.
            System.out.println(e.getMessage());
        }
        return null; // Returns null if an exception occurs.
    }
}