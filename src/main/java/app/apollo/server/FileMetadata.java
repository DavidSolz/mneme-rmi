package app.apollo.server;

import java.time.LocalDateTime;

/**
 * Represents metadata information for a file owned by a user.
 * Includes details such as filename, owner, storage path, block count, and
 * creation timestamp.
 */
public class FileMetadata {

    /** Unique identifier of the file metadata record */
    private Integer id;

    /** Name of the file */
    private String filename;

    /** User ID of the file owner */
    private Integer ownerId;

    /** Filesystem path where the file blocks are stored */
    private String path;

    /** Number of blocks composing the file */
    private Long blockCount;

    /** Timestamp when the file metadata was created */
    private LocalDateTime createdAt;

    /**
     * Default constructor.
     */
    public FileMetadata() {
    }

    /** @return the unique identifier of the file metadata */
    public Integer getId() {
        return id;
    }

    /** @param id the unique identifier to set */
    public void setId(Integer id) {
        this.id = id;
    }

    /** @return the filename */
    public String getFilename() {
        return filename;
    }

    /** @param filename the filename to set */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /** @return the user ID of the owner */
    public Integer getOwnerId() {
        return ownerId;
    }

    /** @param ownerId the user ID of the owner to set */
    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    /** @return the storage path of the file */
    public String getPath() {
        return path;
    }

    /** @param path the storage path to set */
    public void setPath(String path) {
        this.path = path;
    }

    /** @return the creation timestamp */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /** @param createdAt the creation timestamp to set */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /** @return the number of blocks composing the file */
    public Long getBlockCount() {
        return blockCount;
    }

    /** @param blockCount the number of blocks to set */
    public void setBlockCount(Long blockCount) {
        this.blockCount = blockCount;
    }
}
