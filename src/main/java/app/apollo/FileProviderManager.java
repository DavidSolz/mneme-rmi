package app.apollo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import app.common.Block;

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
     * Sets the number of blocks a file contains. If the new count is less than the
     * previous count, removes extra blocks from storage and database.
     *
     * @param userId    ID of the user owning the file.
     * @param filename  Name of the file.
     * @param numBlocks New block count.
     */
    public void setFileBlockCount(Integer userId, String filename, long numBlocks) {
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
            System.err.println("Failed to upload block: " + e.getMessage());
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
                                System.err.println("Failed to delete: " + path + " - " + e.getMessage());
                            }
                        });
            }

            fileBlockDAO.deleteByUserAndFilename(metadata.getOwnerId(), metadata.getId());
            fileMetadataDAO.delete(filename, userId);
        } catch (IOException e) {
            System.err.println("Error deleting file: " + e.getMessage());
        }

    }

    /**
     * Retrieves a list of checksums for all blocks of a file.
     *
     * @param userId   ID of the file owner.
     * @param filename Name of the file.
     * @return List of checksums; empty list if file not found.
     */
    public List<String> getChecksums(Integer userId, String filename) {

        FileMetadata metadata = fileMetadataDAO.findByNameAndOwner(filename, userId);
        ;

        if (metadata == null) {
            return new ArrayList<>();
        }

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
            System.out.println("Nie znaleziono metadanych");
            return null;
        }

        Block block = fileBlockDAO.findByUserFilenameAndBlock(userId, metadata.getId(), blockIndex);
        if (block == null) {
            System.out.println("Nie znaleziono bloku");
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
            System.out.println(e.getMessage());
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
