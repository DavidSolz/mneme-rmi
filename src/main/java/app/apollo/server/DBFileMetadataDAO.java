package app.apollo.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link FileMetadataDAO} to manage file metadata in a SQL
 * database.
 * Provides CRUD operations on the metadata table.
 */
public class DBFileMetadataDAO implements FileMetadataDAO {

    static final Integer CACHE_CAPACITY = 100;

    private Connection connection;

    private final Cache<String, FileMetadata> metadataCache;

    private final Cache<Integer, List<FileMetadata>> ownerFileListCache;

    /**
     * Constructs a new DBFileMetadataDAO with the given SQL connection.
     *
     * @param connection the SQL connection to use for database operations
     */
    public DBFileMetadataDAO(Connection connection) {
        this.connection = connection;
        this.metadataCache = new LRUCache<>(CACHE_CAPACITY);
        this.ownerFileListCache = new LRUCache<>(CACHE_CAPACITY);
    }

    private String makeFileKey(String filename, Integer ownerId) {
        return ownerId + ":" + filename;
    }

    @Override
    public boolean insert(FileMetadata metadata) {

        if (findByNameAndOwner(metadata.getFilename(), metadata.getOwnerId()) != null) {
            return false;
        }

        final String statementString = "INSERT INTO metadata (filename, owner_id, path, created_at, block_count) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(statementString)) {

            statement.setString(1, metadata.getFilename());
            statement.setInt(2, metadata.getOwnerId());
            statement.setString(3, metadata.getPath());
            statement.setString(4, metadata.getCreatedAt().toString());
            statement.setObject(5, metadata.getBlockCount());

            boolean success = statement.executeUpdate() > 0;

            if (success) {
                metadataCache.put(makeFileKey(metadata.getFilename(), metadata.getOwnerId()), metadata);
                ownerFileListCache.remove(metadata.getOwnerId());
            }

            return success;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    @Override
    public boolean update(FileMetadata metadata) {
        final String statementString = "UPDATE metadata SET path=?, created_at=?, block_count=? WHERE filename=? AND owner_id=?";
        try (PreparedStatement statement = connection.prepareStatement(statementString)) {

            statement.setString(1, metadata.getPath());
            statement.setString(2, metadata.getCreatedAt().toString());
            statement.setObject(3, metadata.getBlockCount());
            statement.setString(4, metadata.getFilename());
            statement.setInt(5, metadata.getOwnerId());

            boolean success = statement.executeUpdate() > 0;
            if (success) {
                metadataCache.put(makeFileKey(metadata.getFilename(), metadata.getOwnerId()), metadata);
                ownerFileListCache.remove(metadata.getOwnerId());
            }

            return success;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    @Override
    public void delete(String filename, Integer ownerId) {
        final String statementString = "DELETE FROM metadata WHERE owner_id=? AND filename=?";

        try (PreparedStatement statement = connection.prepareStatement(statementString)) {

            statement.setInt(1, ownerId);
            statement.setString(2, filename);

            statement.executeUpdate();

            metadataCache.remove(makeFileKey(filename, ownerId));
            ownerFileListCache.remove(ownerId);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public List<FileMetadata> findByOwnerId(Integer userId) {
        Optional<List<FileMetadata>> cached = ownerFileListCache.get(userId);

        if (cached.isPresent()) {
            return cached.get();
        }

        final String statementString = "SELECT * FROM metadata WHERE owner_id=?";

        try (PreparedStatement statement = connection.prepareStatement(statementString)) {

            statement.setInt(1, userId);

            ResultSet result = statement.executeQuery();

            List<FileMetadata> metadatas = new ArrayList<>();

            while (result.next()) {
                FileMetadata metadata = new FileMetadata();

                metadata.setId(result.getInt("id"));
                metadata.setFilename(result.getString("filename"));
                metadata.setOwnerId(result.getInt("owner_id"));
                metadata.setPath(result.getString("path"));
                metadata.setCreatedAt(LocalDateTime.parse(result.getString("created_at")));

                metadatas.add(metadata);
            }

            return metadatas;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return new ArrayList<>();
    }

    @Override
    public FileMetadata findByNameAndOwner(String filename, Integer ownerId) {
        Optional<FileMetadata> possibleMetadata = metadataCache.get(makeFileKey(filename, ownerId));

        if (possibleMetadata.isPresent()) {
            return possibleMetadata.get();
        }

        final String statementString = "SELECT * FROM metadata WHERE owner_id=? AND filename=?";

        try (PreparedStatement statement = connection.prepareStatement(statementString)){

            statement.setInt(1, ownerId);
            statement.setString(2, filename);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                FileMetadata metadata = new FileMetadata();

                metadata.setId(result.getInt("id"));
                metadata.setFilename(result.getString("filename"));
                metadata.setOwnerId(result.getInt("owner_id"));
                metadata.setPath(result.getString("path"));
                metadata.setBlockCount(result.getLong("block_count"));
                metadata.setCreatedAt(LocalDateTime.parse(result.getString("created_at")));

                return metadata;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

}
