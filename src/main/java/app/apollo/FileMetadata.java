package app.apollo;

import java.time.LocalDateTime;

public class FileMetadata {

    private Integer id;
    private String filename;
    private Integer ownerId;
    private String path;
    private Long blockCount;
    private LocalDateTime createdAt;

    public FileMetadata() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getBlockCount() {
        return blockCount;
    }

    public void setBlockCount(Long blockCount) {
        this.blockCount = blockCount;
    }

}
