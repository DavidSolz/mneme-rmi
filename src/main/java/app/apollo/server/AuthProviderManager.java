package app.apollo.server;

import java.security.InvalidParameterException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Logger;

import app.apollo.common.Session;

/**
 * Manages user authentication, including login, registration, session
 * validation, and logout.
 * This class acts as a service layer interfacing with DAOs to handle
 * authentication logic.
 */
public class AuthProviderManager {

    private static final Logger logger = Logger.getLogger(AuthProviderManager.class.getName());

    private UserDAO userDAO;

    private SessionDAO sessionDAO;

    /**
     * Constructs an AuthProviderManager using a DAOFactory to access required DAOs.
     *
     * @param factory the DAOFactory to obtain DAO implementations
     */
    public AuthProviderManager(DAOFactory factory) {
        this.userDAO = factory.getUserDAO();
        this.sessionDAO = factory.getSessionDAO();
    }

    /**
     * Logs in a user by username and password.
     * If credentials are valid and an active session exists, returns the existing
     * session.
     * Otherwise, creates a new session with a unique token.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return the active {@link Session} if login successful; null otherwise
     * @throws InvalidParameterException if the credentials are invalid
     */
    public Session login(String username, String password) {

        logger.info("Login attempt for user: " + username);

        User user = userDAO.findByUsername(username);

        if (user == null) {
            logger.warning("Login failed: user '" + username + "' not found.");
            return null;
        }

        if (user.getPassword().equals(password) == false) {
            logger.warning("Login failed: invalid credentials for user '" + username + "'");
            throw new InvalidParameterException("Invalid credentials for user '" + username + "'");
        }

        synchronized (user) {
            Session session = sessionDAO.findByUserId(user.getId());
            if (session != null) {
                logger.info("Existing session reused for user: " + username);
                return session;
            }

            String token = UUID.randomUUID().toString();
            session = new Session();
            session.setUserId(user.getId());
            session.setToken(token);
            session.setCreatedAt(LocalDateTime.now());

            sessionDAO.insert(session);
            logger.info("New session created for user: " + username);

            return session;
        }
    }

    /**
     * Registers a new user with the provided username and password.
     *
     * @param username the username to register
     * @param password the password for the user
     * @return true if registration is successful; false otherwise
     */
    public boolean register(String username, String password) {

        logger.info("Registration attempt for user: " + username);

        User user = new User();

        user.setUsername(username);
        user.setPassword(password);

        boolean result = userDAO.insert(user);

        if (result) {
            logger.info("User '" + username + "' registered successfully.");
        } else {
            logger.warning("Registration failed for user: " + username);
        }

        return result;
    }

    /**
     * Logs out a user by deleting their session associated with the given token.
     *
     * @param token the session token to invalidate
     */
    public void logout(String token) {
        logger.info("Logging out session with token: " + token);
        sessionDAO.delete(token);
    }

    /**
     * Validates a session token.
     * Also cleans up expired sessions older than 15 minutes.
     *
     * @param token the session token to validate
     * @return true if the token corresponds to a valid session; false otherwise
     */
    public boolean validateToken(String token) {
        logger.fine("Validating session token: " + token);

        sessionDAO.deleteExpired(Duration.ofMinutes(15));
        Session session = sessionDAO.findByToken(token);

        boolean valid = session != null;
        logger.fine("Session token '" + token + "' is " + (valid ? "valid" : "invalid"));

        return session != null;
    }

    /**
     * Logs in a user by an existing session token.
     * Returns the session associated with the token if valid.
     *
     * @param token the session token
     * @return the corresponding {@link Session} if token is valid; null otherwise
     */
    public Session login(String token) {
        logger.fine("Login via token: " + token);
        Session session = sessionDAO.findByToken(token);

        if (session == null) {
            logger.warning("Session not found for token: " + token);
        }

        return session;
    }

}
