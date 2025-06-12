package app.apollo.common;

import java.io.Serializable;

/**
 * Represents a data block used for file storage and transfer.
 * <p>
 * A block contains metadata for tracking its origin, integrity (via checksum),
 * and position within a file (via sequence number), as well as the binary data
 * itself.
 * </p>
 *
 * <p>
 * This class is serializable and designed for transmission over RMI.
 * </p>
 */
public class Block implements Serializable {

    /** Computed checksum used for checking differences */
    private String checksum;

    /** Computed fingerprint used for checking differences */
    private String fingerprint;

    /** Metadata Id that refers to metadata object */
    private Integer metadataId;

    /** User Id that refers to user object */
    private Integer userId;

    /** Number in the sequence of blocks */
    private Long sequenceNumber;

    /** Byte array that stores data */
    private byte[] data;

    /** Total size of block */
    private Integer size;

    /**
     * Returns the checksum of this block used for integrity verification.
     *
     * @return A hexadecimal string representing the checksum.
     */
    public String getChecksum() {
        return checksum;
    }

    /**
     * Sets the checksum of this block.
     *
     * @param checksum A hexadecimal string representing the block's checksum.
     */
    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    /**
     * Returns the metadata ID associated with this block.
     *
     * @return An integer representing the metadata ID.
     */
    public Integer getMetadataId() {
        return metadataId;
    }

    /**
     * Sets the metadata ID for this block.
     *
     * @param metadataId The metadata identifier.
     */
    public void setMetadataId(Integer metadataId) {
        this.metadataId = metadataId;
    }

    /**
     * Returns the user ID of the owner of this block.
     *
     * @return An integer representing the user ID.
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * Sets the user ID associated with this block.
     *
     * @param userId The ID of the user who owns this block.
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * Returns the sequence number of this block within its file.
     *
     * @return The zero-based index of the block.
     */
    public Long getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Sets the sequence number of this block.
     *
     * @param sequenceNumber The index of the block in the file.
     */
    public void setSequenceNumber(Long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * Returns the binary data of this block.
     *
     * @return A byte array containing the block's data.
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Sets the binary data for this block.
     *
     * @param data A byte array containing the block's contents.
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * Returns the size of the block's data in bytes.
     *
     * @return An integer representing the size of the block.
     */
    public Integer getSize() {
        return size;
    }

    /**
     * Sets the size of the block's data in bytes.
     *
     * @param size The size of the data payload.
     */
    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * Returns the fingerprint of this block used for integrity verification.
     *
     * @return A hexadecimal string representing the fingerprint.
     */
    public String getFingerprint() {
        return fingerprint;
    }

    /**
     * Sets the fingerprint of this block.
     *
     * @param checksum A hexadecimal string representing the block's fingerprint.
     */
    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }
}
