package app.apollo;

/**
 * Represents a user entity in the system.
 * <p>
 * This class holds basic user account details such as ID, username, and password.
 * It's typically used with a {@link UserDAO} for persistence and retrieval.
 * </p>
 */
public class User {

    /** Unique identifier for the user (e.g., database primary key). */
    private Integer id;

    /** Unique username for login and identification. */
    private String username;

    /** User's password (stored in hashed form in practice). */
    private String password;

    /**
     * Default constructor.
     */
    public User() {}

    /**
     * Returns the user's unique ID.
     *
     * @return the user ID.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the user's unique ID.
     *
     * @param id the user ID to set.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Returns the username of the user.
     *
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for the user.
     *
     * @param username the username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the password of the user.
     * <p><b>Note:</b> Passwords should be stored in hashed form for security.</p>
     *
     * @return the user's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the user.
     * <p><b>Note:</b> This should be hashed before being stored or compared.</p>
     *
     * @param password the password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
