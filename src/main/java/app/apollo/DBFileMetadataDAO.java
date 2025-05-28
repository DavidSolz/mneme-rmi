package app.apollo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DBFileMetadataDAO implements FileMetadataDAO {

    private Connection connection;

    public DBFileMetadataDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean insert(FileMetadata metadata) {
        final String statementString = "INSERT INTO metadata (filename, owner_id, path, created_at, block_count) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = null;

        if (findByNameAndOwner(metadata.getFilename(), metadata.getOwnerId()) != null) {
            return false;
        }

        try {
            statement = connection.prepareStatement(statementString);

            statement.setString(1, metadata.getFilename());
            statement.setInt(2, metadata.getOwnerId());
            statement.setString(3, metadata.getPath());
            statement.setString(4, metadata.getCreatedAt().toString());
            statement.setObject(5, metadata.getBlockCount());

            return statement.executeUpdate() > 0;

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
            statement.setObject(3, metadata.getBlockCount()); // Can be null
            statement.setString(4, metadata.getFilename());
            statement.setInt(5, metadata.getOwnerId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    @Override
    public void delete(String filename, Integer ownerId) {
        final String statementString = "DELETE FROM metadata WHERE owner_id=? AND filename=?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(statementString);

            statement.setInt(1, ownerId);
            statement.setString(2, filename);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public List<FileMetadata> findByOwnerId(Integer userId) {
        final String statementString = "SELECT * FROM metadata WHERE owner_id=?";
        PreparedStatement statement = null;
        List<FileMetadata> metadatas = null;

        try {
            statement = connection.prepareStatement(statementString);

            statement.setInt(1, userId);

            ResultSet result = statement.executeQuery();

            metadatas = new ArrayList<>();

            while (result.next()) {
                FileMetadata metadata = new FileMetadata();

                metadata.setId(result.getInt("id"));
                metadata.setFilename(result.getString("filename"));
                metadata.setOwnerId(result.getInt("owner_id"));
                metadata.setPath(result.getString("path"));
                metadata.setCreatedAt(LocalDateTime.parse(result.getString("created_at")));

                metadatas.add(metadata);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return metadatas;
    }

    @Override
    public FileMetadata findByNameAndOwner(String filename, Integer ownerId) {
        final String statementString = "SELECT * FROM metadata WHERE owner_id=? AND filename=?";
        PreparedStatement statement = null;
        FileMetadata metadata = null;

        try {
            statement = connection.prepareStatement(statementString);

            statement.setInt(1, ownerId);
            statement.setString(2, filename);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                metadata = new FileMetadata();

                metadata.setId(result.getInt("id"));
                metadata.setFilename(result.getString("filename"));
                metadata.setOwnerId(result.getInt("owner_id"));
                metadata.setPath(result.getString("path"));
                metadata.setCreatedAt(LocalDateTime.parse(result.getString("created_at")));

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return metadata;
    }

}
