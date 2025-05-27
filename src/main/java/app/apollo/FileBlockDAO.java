package app.apollo;

import java.util.List;

import app.common.Block;

public interface FileBlockDAO {

    public boolean insert(Block block);
    public Block findByUserFilenameAndBlock(Integer userId, Integer fileId, Long blockId);
    public Block findByMetadataId(Integer metadataId);
    public List<String> findChecksumByUserAndFilename(Integer userId, Integer fileId);
    public void deleteByUserFilenameAndBlock(Integer userId, Integer fileId, Long blockId);
    public void deleteByUserAndFilename(Integer userId, Integer fileId);
}
