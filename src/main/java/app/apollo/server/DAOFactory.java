package app.apollo.server;

/**
 * Factory interface to provide Data Access Objects (DAOs) for different
 * entities.
 * Implementations of this interface supply DAO instances for sessions, users,
 * file metadata, and file blocks.
 */
public interface DAOFactory {

    /**
     * Returns the Data Access Object for session-related operations.
     *
     * @return an implementation of {@link SessionDAO}
     */
    SessionDAO getSessionDAO();

    /**
     * Returns the Data Access Object for user-related operations.
     *
     * @return an implementation of {@link UserDAO}
     */
    UserDAO getUserDAO();

    /**
     * Returns the Data Access Object for file metadata-related operations.
     *
     * @return an implementation of {@link FileMetadataDAO}
     */
    FileMetadataDAO getFileMetadataDAO();

    /**
     * Returns the Data Access Object for file block-related operations.
     *
     * @return an implementation of {@link FileBlockDAO}
     */
    FileBlockDAO getFileBlockDAO();
}
