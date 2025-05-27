package app.apollo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import app.common.Block;

public class FileProviderManager {

    private FileMetadataDAO fileMetadataDAO;
    private FileBlockDAO fileBlockDAO;

    public FileProviderManager(DAOFactory factory) {
        this.fileMetadataDAO = factory.getFileMetadataDAO();
        this.fileBlockDAO = factory.getFileBlockDAO();
    }

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

    public List<String> getChecksums(Integer userId, String filename) {

        FileMetadata metadata = fileMetadataDAO.findByNameAndOwner(filename, userId);
        ;

        if (metadata == null) {
            return new ArrayList<>();
        }

        return fileBlockDAO.findChecksumByUserAndFilename(userId, metadata.getId());
    }

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
