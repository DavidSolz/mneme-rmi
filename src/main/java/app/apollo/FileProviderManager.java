package app.apollo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileProviderManager {

    private FileMetadataDAO fileMetadataDAO;

    public FileProviderManager(DAOFactory factory)
    {
        this.fileMetadataDAO = factory.getFileMetadataDAO();
    }

    public void uploadBlock(Integer userId, String filename, byte[] data, long size, long blockIndex) {

        FileMetadata metadata = null;
        String path = "";

        // TODO
        path = userId.toString() + "/" + filename;
        metadata = new FileMetadata();

        metadata.setOwnerId(userId);
        metadata.setFilename(filename);
        metadata.setPath(path);
        metadata.setCreatedAt(LocalDateTime.now());

        fileMetadataDAO.insert(metadata);
    }

    public void deleteFile(Integer userId, String filename) {

        fileMetadataDAO.delete(filename, userId);
        // TODO
    }

    public List<String> getChecksums(Integer userId, String filename) {

        List<String> checksums = null;

        // TODO

        return checksums;
    }

    public byte[] downloadBlock(Integer userId, String filename, long blockIndex) {

        byte[] block = null;

        // TODO

        return block;
    }

    public List<String> listFiles(Integer userId) {

        List<String> filenames = null;

        List<FileMetadata> metadatas = fileMetadataDAO.findByOwnerId(userId);

        if( metadatas == null )
        {
            return null;
        }

        filenames = new ArrayList<>(metadatas.size());

        Integer index = 0;
        for( FileMetadata metadata : metadatas )
        {
            filenames.add(index++, metadata.getFilename());
        }

        return filenames;
    }

}
