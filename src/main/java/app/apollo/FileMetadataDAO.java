package app.apollo;

import java.util.List;

public interface FileMetadataDAO {

    public boolean insert(FileMetadata metadata);
    public void delete(String filename, Integer ownerId);
    public List<FileMetadata> findByOwnerId(Integer userId);
    public FileMetadata findByNameAndOwner(String filename, Integer ownerId);
}
