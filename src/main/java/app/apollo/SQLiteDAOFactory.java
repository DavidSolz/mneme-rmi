package app.apollo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * SQLite-specific implementation of the {@link DAOFactory}.
 * <p>
 * This factory sets up a JDBC connection to an SQLite database
 * and provides DAO instances that share this connection.
 * </p>
 */
public class SQLiteDAOFactory implements DAOFactory {

    /** Shared SQLite database connection for all DAO instances. */
    private Connection connection;

    /**
     * Constructs a new {@code SQLiteDAOFactory} with the given SQLite database URL.
     *
     * @param url the JDBC connection string (e.g., "jdbc:sqlite:mydb.db").
     */
    public SQLiteDAOFactory(String url) {
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.err.println("Failed to connect to SQLite database: " + e.getMessage());
        }
    }

    /**
     * Provides a DAO for managing user sessions.
     *
     * @return a new instance of {@link DBSessionDAO}.
     */
    @Override
    public SessionDAO getSessionDAO() {
        return new DBSessionDAO(connection);
    }

    /**
     * Provides a DAO for managing users.
     *
     * @return a new instance of {@link DBUserDAO}.
     */
    @Override
    public UserDAO getUserDAO() {
        return new DBUserDAO(connection);
    }

    /**
     * Provides a DAO for managing file metadata.
     *
     * @return a new instance of {@link DBFileMetadataDAO}.
     */
    @Override
    public FileMetadataDAO getFileMetadataDAO() {
        return new DBFileMetadataDAO(connection);
    }

    /**
     * Provides a DAO for managing file blocks.
     *
     * @return a new instance of {@link DBFileBlockDAO}.
     */
    @Override
    public FileBlockDAO getFileBlockDAO() {
        return new DBFileBlockDAO(connection);
    }
}
