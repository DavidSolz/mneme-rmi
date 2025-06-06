package app.apollo.server;

import java.util.List;

/**
 * Data Access Object (DAO) interface for managing file metadata.
 * Provides methods to insert, update, delete, and query metadata entries.
 */
public interface FileMetadataDAO {

    /**
     * Inserts a new file metadata record into the data store.
     *
     * @param metadata The file metadata to insert.
     * @return {@code true} if insertion was successful, {@code false} otherwise.
     */
    public boolean insert(FileMetadata metadata);

    /**
     * Updates an existing file metadata record.
     *
     * @param metadata The updated file metadata.
     * @return {@code true} if the update was successful, {@code false} otherwise.
     */
    public boolean update(FileMetadata metadata);

    /**
     * Deletes the metadata record for a file owned by a specific user.
     *
     * @param filename The name of the file.
     * @param ownerId  The ID of the owner of the file.
     */
    public void delete(String filename, Integer ownerId);

    /**
     * Finds all file metadata entries belonging to a specific user.
     *
     * @param userId The user ID.
     * @return List of {@link FileMetadata} objects owned by the user.
     */
    public List<FileMetadata> findByOwnerId(Integer userId);

    /**
     * Finds metadata for a file by filename and owner ID.
     *
     * @param filename The name of the file.
     * @param ownerId  The ID of the owner.
     * @return The {@link FileMetadata} matching the filename and owner, or {@code null} if none found.
     */
    public FileMetadata findByNameAndOwner(String filename, Integer ownerId);
}
