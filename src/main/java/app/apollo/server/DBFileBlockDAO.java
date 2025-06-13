package app.apollo.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import app.apollo.common.Block;
import app.apollo.common.FrozenPair;

/**
 * Implementation of {@link FileBlockDAO} that interacts with a SQL database to
 * perform CRUD operations on file blocks.
 */
public class DBFileBlockDAO implements FileBlockDAO {

    static final int CACHE_CAPACITY = 100;

    private Connection connection;

    private final Cache<String, Block> blockCache;

    private final Cache<String, List<FrozenPair<String, String>>> checksumCache;

    /**
     * Constructs a new DBFileBlockDAO with the given database connection.
     *
     * @param connection the SQL connection to be used for database operations
     */
    public DBFileBlockDAO(Connection connection) {
        this.connection = connection;
        this.blockCache = new LRUCache<>(CACHE_CAPACITY);
        this.checksumCache = new LRUCache<>(CACHE_CAPACITY);
    }

    private String blockKey(int userId, int metadataId, long sequenceId) {
        return userId + ":" + metadataId + ":" + sequenceId;
    }

    private String checksumKey(int userId, int metadataId) {
        return userId + ":" + metadataId + ":checksums";
    }

    @Override
    public boolean insert(Block block) {
        String sql = "INSERT INTO blocks (user_id, metadata_id, sequence_id, size, checksum, fingerprint) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT(user_id, metadata_id, sequence_id) DO UPDATE SET size = excluded.size, checksum = excluded.checksum";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, block.getUserId());
            stmt.setInt(2, block.getMetadataId());
            stmt.setLong(3, block.getSequenceNumber());
            stmt.setInt(4, block.getSize());
            stmt.setString(5, block.getChecksum());
            stmt.setString(6, block.getFingerprint());

            stmt.executeUpdate();

            blockCache.put(blockKey(block.getUserId(), block.getMetadataId(), block.getSequenceNumber()), block);
            checksumCache.remove(checksumKey(block.getUserId(), block.getMetadataId()));

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Block findByUserFilenameAndBlock(Integer userId, Integer metadataId, Long blockId) {
        String key = blockKey(userId, metadataId, blockId);
        Optional<Block> cached = blockCache.get(key);
        if (cached.isPresent()) {
            return cached.get();
        }

        String sql = "SELECT * FROM blocks WHERE user_id = ? AND metadata_id = ? AND sequence_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, metadataId);
            stmt.setLong(3, blockId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Block block = extractBlock(rs);
                blockCache.put(key, block);
                return block;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<FrozenPair<String, String>> findChecksumByUserAndFilename(Integer userId, Integer metadataId) {
        String key = checksumKey(userId, metadataId);
        Optional<List<FrozenPair<String, String>>> cached = checksumCache.get(key);
        if (cached.isPresent()) {
            return cached.get();
        }

        String sql = "SELECT fingerprint, checksum FROM blocks WHERE user_id = ? AND metadata_id = ? ORDER BY sequence_id";
        List<FrozenPair<String, String>> checksums = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, metadataId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                checksums.add(new FrozenPair<>(rs.getString("fingerprint"), rs.getString("checksum")));
            }

            checksumCache.put(key, checksums);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return checksums;
    }

    @Override
    public void deleteByUserFilenameAndBlock(Integer userId, Integer metadataId, Long sequenceId) {
        String sql = "DELETE FROM blocks WHERE user_id = ? AND metadata_id = ? AND sequence_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, metadataId);
            stmt.setLong(3, sequenceId);
            stmt.executeUpdate();

            blockCache.remove(blockKey(userId, metadataId, sequenceId));
            checksumCache.remove(checksumKey(userId, metadataId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteByUserAndFilename(Integer userId, Integer metadataId) {
        String sql = "DELETE FROM blocks WHERE user_id = ? AND metadata_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, metadataId);
            stmt.executeUpdate();

            checksumCache.remove(checksumKey(userId, metadataId));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Extracts a Block object from the current row of a ResultSet.
     *
     * @param rs the ResultSet positioned at a row representing a block
     * @return the extracted Block object
     * @throws SQLException if a database access error occurs
     */
    private Block extractBlock(ResultSet rs) throws SQLException {
        Block block = new Block();
        block.setUserId(rs.getInt("user_id"));
        block.setMetadataId(rs.getInt("metadata_id"));
        block.setSequenceNumber(rs.getLong("sequence_id"));
        block.setChecksum(rs.getString("checksum"));
        block.setFingerprint(rs.getString("fingerprint"));
        block.setSize(rs.getInt("size"));
        return block;
    }

    @Override
    public void deleteByMetadataIdAndSequence(Integer metadataId, Long sequenceId) {
        String sql = "DELETE FROM blocks WHERE metadata_id = ? AND sequence_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, metadataId);
            stmt.setLong(2, sequenceId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
