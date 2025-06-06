package app.apollo.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface for user authentication service using Java RMI.
 * <p>
 * This interface provides methods for user registration, login, logout,
 * and session token validation.
 * </p>
 */
public interface AuthService extends Remote {

    /**
     * Registers a new user in the system.
     *
     * @param username The username (login) of the new user.
     * @param password The password of the new user.
     * @return {@code true} if the registration was successful; {@code false}
     *         otherwise.
     * @throws RemoteException If a remote communication error occurs.
     */
    boolean register(String username, String password) throws RemoteException;

    /**
     * Logs a user into the system using their username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return A {@link Session} object representing the authenticated session.
     * @throws RemoteException If a remote communication error occurs.
     */
    Session login(String username, String password) throws RemoteException;

    /**
     * Logs a user into the system using an existing session token.
     *
     * @param token The session token of the user.
     * @return A {@link Session} object representing the session associated with the
     *         token.
     * @throws RemoteException If a remote communication error occurs.
     */
    Session login(String token) throws RemoteException;

    /**
     * Logs a user out of the system, invalidating their session token.
     *
     * @param token The session token of the user to log out.
     * @throws RemoteException If a remote communication error occurs.
     */
    void logout(String token) throws RemoteException;

    /**
     * Validates whether a given session token is active and recognized.
     *
     * @param token The session token to validate.
     * @return {@code true} if the token is valid and active; {@code false}
     *         otherwise.
     * @throws RemoteException If a remote communication error occurs.
     */
    boolean validateToken(String token) throws RemoteException;
}
