package app.apollo.client;

/**
 * SessionContext represents the context of a user session.
 * It holds the session token used for authentication and authorization.
 */
public class SessionContext {
    private String token; // The session token associated with the user.

    /**
     * Sets the session token.
     * 
     * @param token The session token to set.
     */
    public void setToken(String token) {
        this.token = token; // Assign the provided token to the instance variable.
    }

    /**
     * Retrieves the session token.
     * 
     * @return The session token as a String.
     */
    public String getToken() {
        return this.token; // Return the stored session token.
    }
}
