package app.apollo.server;

import java.util.List;

import app.apollo.common.Block;
import app.apollo.common.FrozenPair;

/**
 * Data Access Object (DAO) interface for managing file blocks.
 * Provides methods for inserting, querying, and deleting blocks of files.
 */
public interface FileBlockDAO {

    /**
     * Inserts a new block into the data store.
     *
     * @param block The block to insert.
     * @return {@code true} if the insertion was successful, {@code false}
     *         otherwise.
     */
    public boolean insert(Block block);

    /**
     * Finds a block by user ID, file ID, and block sequence number.
     *
     * @param userId  The ID of the user who owns the file.
     * @param fileId  The ID of the file metadata.
     * @param blockId The sequence number of the block.
     * @return The {@link Block} object if found, otherwise {@code null}.
     */
    public Block findByUserFilenameAndBlock(Integer userId, Integer fileId, Long blockId);

    /**
     * Retrieves a list of checksums for all blocks of a file for a given user.
     *
     * @param userId The ID of the user who owns the file.
     * @param fileId The ID of the file metadata.
     * @return List of checksum strings for each block of the file.
     */
    public List<FrozenPair<String, String>> findChecksumByUserAndFilename(Integer userId, Integer fileId);

    /**
     * Deletes a block by metadata ID and sequence number.
     *
     * @param metadataId The ID of the file metadata.
     * @param sequenceId The sequence number of the block.
     */
    public void deleteByMetadataIdAndSequence(Integer metadataId, Long sequenceId);

    /**
     * Deletes a specific block of a file for a user.
     *
     * @param userId  The ID of the user.
     * @param fileId  The ID of the file metadata.
     * @param blockId The sequence number of the block to delete.
     */
    public void deleteByUserFilenameAndBlock(Integer userId, Integer fileId, Long blockId);

    /**
     * Deletes all blocks of a file for a specific user.
     *
     * @param userId The ID of the user.
     * @param fileId The ID of the file metadata.
     */
    public void deleteByUserAndFilename(Integer userId, Integer fileId);
}
