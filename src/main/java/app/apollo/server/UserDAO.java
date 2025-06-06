package app.apollo.server;

/**
 * Data Access Object (DAO) interface for user-related database operations.
 * <p>
 * This interface defines methods for retrieving and persisting {@link User}
 * entities.
 * It abstracts the underlying data store, allowing for different
 * implementations
 * (e.g., in-memory, JDBC, ORM).
 * </p>
 */
public interface UserDAO {

    /**
     * Finds a user by their username.
     *
     * @param username The username to search for.
     * @return The {@link User} if found, or {@code null} if no match exists.
     */
    public User findByUsername(String username);

    /**
     * Inserts a new user into the data store.
     *
     * @param user The user to insert.
     * @return {@code true} if insertion succeeded, {@code false} otherwise.
     */
    public boolean insert(User user);

    /**
     * Finds a user by their unique ID.
     *
     * @param id The unique identifier of the user.
     * @return The {@link User} if found, or {@code null} if not present.
     */
    public User findById(Integer id);
}
