package app.apollo.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import app.apollo.common.Block;
import app.apollo.common.FrozenPair;

/**
 * Manages file metadata and blocks, coordinating storage operations and
 * persistence.
 * <p>
 * This class acts as a service layer between the remote file provider and the
 * data access objects (DAOs) for file metadata and blocks.
 * It handles creation, update, retrieval, and deletion of file blocks and
 * metadata.
 * </p>
 */
public class FileProviderManager {

    private FileMetadataDAO fileMetadataDAO;
    private FileBlockDAO fileBlockDAO;

    private static final Logger logger = Logger.getLogger(FileProviderManager.class.getName());

    // ** Locks keyed by "userId:filename" */
    private final ConcurrentHashMap<String, ReentrantLock> fileLocks = new ConcurrentHashMap<>();

    /**
     * Constructs a FileProviderManager with the specified DAOFactory.
     *
     * @param factory DAOFactory to obtain DAO instances for metadata and blocks.
     */
    public FileProviderManager(DAOFactory factory) {
        this.fileMetadataDAO = factory.getFileMetadataDAO();
        this.fileBlockDAO = factory.getFileBlockDAO();
    }

    /**
     * Provides a thread safe lock when managing same file
     *
     * @param userId
     * @param filename
     * @return Lock object that ensuers thread safety
     */
    private ReentrantLock getLock(Integer userId, String filename) {
        String key = userId + ":" + filename;
        fileLocks.putIfAbsent(key, new ReentrantLock());
        return fileLocks.get(key);
    }

    /**
     * Sets the number of blocks a file contains. If the new count is less than the
     * previous count, removes extra blocks from storage and database.
     *
     * @param userId    ID of the user owning the file.
     * @param filename  Name of the file.
     * @param numBlocks New block count.
     */
    public void setFileBlockCount(Integer userId, String filename, long numBlocks) {

        ReentrantLock lock = getLock(userId, filename);
        lock.lock();

        logger.info("Setting block count for file '" + filename + "' for user " + userId);

        try {
            FileMetadata metadata = fileMetadataDAO.findByNameAndOwner(filename, userId);

            if (metadata != null) {
                long oldBlockCount = metadata.getBlockCount();

                if (numBlocks < oldBlockCount) {
                    for (long i = numBlocks; i < oldBlockCount; i++) {
                        Path blockPath = Paths.get(metadata.getPath(), String.valueOf(i));

                        try {
                            Files.deleteIfExists(blockPath);
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }

                        fileBlockDAO.deleteByMetadataIdAndSequence(metadata.getId(), i);
                    }
                }

                metadata.setBlockCount(numBlocks);
                fileMetadataDAO.update(metadata);
            } else {
                metadata = new FileMetadata();
                metadata.setOwnerId(userId);
                metadata.setFilename(filename);
                metadata.setCreatedAt(LocalDateTime.now());

                Path baseDir = Paths.get("storage", String.valueOf(userId), filename);
                metadata.setPath(baseDir.toString());
                metadata.setBlockCount(numBlocks);

                fileMetadataDAO.insert(metadata);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Retrieves the number of blocks for the specified file.
     *
     * @param userId   ID of the file owner.
     * @param filename Name of the file.
     * @return The number of blocks for the file or 0 if metadata not found.
     */
    public long getFileBlockCount(Integer userId, String filename) {
        FileMetadata metadata = fileMetadataDAO.findByNameAndOwner(filename, userId);

        logger.info("Getting block count for file '" + filename + "' for user " + userId);

        if (metadata != null && metadata.getBlockCount() != null) {
            return metadata.getBlockCount();
        }

        return 0;
    }

    /**
     * Uploads a block for the specified file. Creates file metadata if missing.
     * Stores block data on disk and inserts block info into database.
     *
     * @param userId   ID of the user uploading the block.
     * @param filename Name of the file.
     * @param block    Block data and metadata.
     */
    public void uploadBlock(Integer userId, String filename, Block block) {

        ReentrantLock lock = getLock(userId, filename);
        lock.lock();

        logger.info("Uploading block " + block.getSequenceNumber() + " for file '" + filename + "' by user " + userId);

        try {
            FileMetadata metadata = fileMetadataDAO.findByNameAndOwner(filename, userId);

            if (metadata == null) {
                metadata = new FileMetadata();
                metadata.setOwnerId(userId);
                metadata.setFilename(filename);
                metadata.setCreatedAt(LocalDateTime.now());

                Path baseDir = Paths.get("storage", String.valueOf(userId), filename);
                metadata.setPath(baseDir.toString());

                fileMetadataDAO.insert(metadata);
                metadata = fileMetadataDAO.findByNameAndOwner(filename, userId);
            }

            Path blockPath = Paths.get(metadata.getPath(), String.valueOf(block.getSequenceNumber()));

            try {
                Files.createDirectories(blockPath.getParent());
                Files.write(blockPath, block.getData());

                block.setMetadataId(metadata.getId());
                block.setUserId(metadata.getOwnerId());

                fileBlockDAO.insert(block);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Failed to upload block", e);
            }
        } finally {
            lock.unlock();
        }

    }

    /**
     * Deletes the specified file along with all its blocks from both disk and
     * database.
     *
     * @param userId   ID of the file owner.
     * @param filename Name of the file to delete.
     */
    public void deleteFile(Integer userId, String filename) {

        ReentrantLock lock = getLock(userId, filename);
        lock.lock();

        logger.info("Deleting file '" + filename + "' for user " + userId);

        try {
            FileMetadata metadata = fileMetadataDAO.findByNameAndOwner(filename, userId);

            if (metadata == null) {
                return;
            }

            try {
                Path fileDir = Paths.get(metadata.getPath()).getParent();
                if (fileDir != null && Files.exists(fileDir)) {
                    Files.walk(fileDir)
                            .sorted((a, b) -> b.compareTo(a))
                            .forEach(path -> {
                                try {
                                    Files.deleteIfExists(path);
                                } catch (IOException e) {
                                    logger.log(Level.WARNING, "Failed to delete: " + path, e);

                                }
                            });
                }

                fileBlockDAO.deleteByUserAndFilename(metadata.getOwnerId(), metadata.getId());
                fileMetadataDAO.delete(filename, userId);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error deleting file", e);
            }
        } finally {
            lock.unlock();
        }

    }

    /**
     * Retrieves a list of checksums for all blocks of a file.
     *
     * @param userId   ID of the file owner.
     * @param filename Name of the file.
     * @return List of checksums; empty list if file not found.
     */
    public List<FrozenPair<String, String>> getChecksums(Integer userId, String filename) {

        FileMetadata metadata = fileMetadataDAO.findByNameAndOwner(filename, userId);

        if (metadata == null) {
            logger.warning("Metadata not found for file '" + filename + "'");
            return new ArrayList<>();
        }

        logger.info("Fetching checksums for '" + filename + "' for user " + userId);

        return fileBlockDAO.findChecksumByUserAndFilename(userId, metadata.getId());
    }

    /**
     * Downloads a specific block of a file.
     * Reads block data from disk and returns a Block object with its data.
     *
     * @param userId     ID of the file owner.
     * @param filename   Name of the file.
     * @param blockIndex Index of the block to download.
     * @return Block containing the data and metadata; null if not found or error.
     */
    public Block downloadBlock(Integer userId, String filename, long blockIndex) {

        FileMetadata metadata = fileMetadataDAO.findByNameAndOwner(filename, userId);
        if (metadata == null) {
            logger.warning("Metadata not found for user " + userId + " and file '" + filename + "'");
            return null;
        }

        Block block = fileBlockDAO.findByUserFilenameAndBlock(userId, metadata.getId(), blockIndex);
        if (block == null) {
            logger.warning("Block not found for file '" + filename + "', block " + blockIndex);
            return null;
        }

        Path blockPath = Paths.get(metadata.getPath(), String.valueOf(blockIndex));

        try {
            byte[] data = Files.readAllBytes(blockPath);
            block.setData(data);
            block.setSize(data.length);
            block.setSequenceNumber(blockIndex);

            return block;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to read block from disk", e);
        }

        return null;
    }

    /**
     * Lists all filenames owned by the specified user.
     *
     * @param userId ID of the user.
     * @return List of filenames belonging to the user; empty if none found.
     */
    public List<String> listFiles(Integer userId) {

        List<FileMetadata> metadatas = fileMetadataDAO.findByOwnerId(userId);
        List<String> filenames = new ArrayList<>();

        if (metadatas != null) {
            for (FileMetadata metadata : metadatas) {
                filenames.add(metadata.getFilename());
            }
        }

        return filenames;
    }

}
