package app.apollo.server;

import java.time.Duration;

import app.apollo.common.Session;

/**
 * Data Access Object (DAO) interface for managing user sessions.
 * <p>
 * Defines methods for retrieving, inserting, and deleting {@link Session} objects,
 * as well as cleaning up expired sessions.
 * </p>
 */
public interface SessionDAO {

    /**
     * Finds a session by its unique token.
     *
     * @param token The session token.
     * @return The {@link Session} associated with the token, or {@code null} if not found.
     */
    public Session findByToken(String token);

    /**
     * Finds a session associated with a specific user ID.
     *
     * @param id The user ID.
     * @return The {@link Session} for the user, or {@code null} if no session exists.
     */
    public Session findByUserId(Integer id);

    /**
     * Inserts a new session into the data store.
     *
     * @param session The {@link Session} to insert.
     * @return {@code true} if the insertion was successful, {@code false} otherwise.
     */
    public boolean insert(Session session);

    /**
     * Deletes a session by its token.
     *
     * @param token The token of the session to delete.
     */
    public void delete(String token);

    /**
     * Deletes all sessions that are older than the specified maximum age.
     *
     * @param maxAge The maximum allowed session age.
     */
    public void deleteExpired(Duration maxAge);
}
