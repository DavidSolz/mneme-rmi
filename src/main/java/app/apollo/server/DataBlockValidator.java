package app.apollo.server;

import app.apollo.common.Block;
import app.apollo.common.CrypticEngine;

/***
 * DataBlockValidator class is an implementation of BlockValidator interface that checks if a given block is valid.
 */
public class DataBlockValidator implements BlockValidator {

    /** Number of bytes that a block stores */
    private final Integer blockSize;

    /***
     * Constructs a DataBlockValidator with a required block size.
     * @param blockSize Number of bytes that block stores; must be non-null and positive.
     */
    public DataBlockValidator(Integer blockSize) {
        if (blockSize == null || blockSize <= 0) {
            throw new IllegalArgumentException("blockSize must be a positive integer.");
        }
        this.blockSize = blockSize;
    }

    @Override
    public boolean isBlockValid(Block block) {

        if (block == null) {
            return false;
        }

        if (block.getData() == null || block.getFingerprint() == null || block.getChecksum() == null) {
            return false;
        }

        if (block.getUserId() <= 0 || block.getSize() <= 0) {
            return false;
        }

        byte[] data = block.getData();

        if (data.length != blockSize) {
            return false;
        }

        String computedFingerprint = CrypticEngine.weakHash(data, blockSize);

        if (!block.getFingerprint().equals(computedFingerprint)) {
            return false;
        }

        String computedChecksum = CrypticEngine.strongHash(computedFingerprint, data, blockSize);

        if (!block.getChecksum().equals(computedChecksum)) {
            return false;
        }

        return true;
    }
}
