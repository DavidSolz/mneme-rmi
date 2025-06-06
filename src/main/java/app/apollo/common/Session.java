package app.apollo.common;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a user session in the system.
 * <p>
 * This class holds information about an authenticated session, including a
 * unique token,
 * the associated user ID, and the timestamp when the session was created.
 * </p>
 *
 * <p>
 * Used by authentication and authorization components to identify and manage
 * user sessions.
 * </p>
 *
 * @see AuthService
 */
public class Session implements Serializable {

    /** Unique string thet represents the session */
    private String token;

    /** Number referencing the user */
    private Integer userId;

    /** Timestamp when the session was created */
    private LocalDateTime createdAt;

    /**
     * Returns the session token.
     *
     * @return The unique session token as a {@code String}.
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the session token.
     *
     * @param token A unique string representing the session token.
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Returns the user ID associated with this session.
     *
     * @return The user ID as an {@code Integer}.
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * Sets the user ID for this session.
     *
     * @param userId The ID of the user this session belongs to.
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * Returns the timestamp when the session was created.
     *
     * @return A {@link LocalDateTime} object representing session creation time.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the session creation timestamp.
     *
     * @param date The date and time when the session was created.
     */
    public void setCreatedAt(LocalDateTime date) {
        this.createdAt = date;
    }
}
