package app.apollo.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import app.apollo.common.Session;

/**
 * Data Access Object (DAO) implementation for accessing and manipulating
 * User records in the database.
 */
public class DBSessionDAO implements SessionDAO {

    static final Integer CACHE_CAPACITY = 100;

    private Connection connection;

    private final Cache<String, Session> sessionCache;

    /**
     * Constructs a new DBUserDAO with the given database connection.
     *
     * @param connection the active database connection
     */
    public DBSessionDAO(Connection connection) {
        this.connection = connection;
        this.sessionCache = new LRUCache<>(CACHE_CAPACITY);
        clearAllSessions();
    }

    private void clearAllSessions() {
        final String sql = "DELETE FROM sessions";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            sessionCache.clear();

        } catch (SQLException e) {
            System.out.println("Failed to clear sessions: " + e.getMessage());
        }
    }

    @Override
    public Session findByToken(String token) {

        Optional<Session> possibleSession = sessionCache.get(token);

        if (possibleSession.isPresent()) {
            return possibleSession.get();
        }

        final String statementString = "SELECT * FROM sessions WHERE token=?";
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(statementString);

            statement.setString(1, token);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                Session session = new Session();

                session.setUserId(result.getInt("user_id"));
                session.setToken(result.getString("token"));
                session.setCreatedAt(LocalDateTime.parse(result.getString("created_at")));

                sessionCache.put(token, session);

                return session;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    @Override
    public boolean insert(Session session) {
        final String statementString = "INSERT INTO sessions (user_id, token, created_at) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(statementString)) {

            statement.setInt(1, session.getUserId());
            statement.setString(2, session.getToken());
            statement.setString(3, session.getCreatedAt().toString());

            boolean inserted = statement.execute();

            if (inserted) {
                sessionCache.put(session.getToken(), session);
            }

            return inserted;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    @Override
    public void delete(String token) {

        final String statementString = "DELETE FROM sessions WHERE token = ?";

        try (PreparedStatement statement = connection.prepareStatement(statementString)) {

            statement.setString(1, token);

            statement.executeUpdate();

            sessionCache.remove(token);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void deleteExpired(Duration maxAge) {
        LocalDateTime cutoff = LocalDateTime.now().minus(maxAge);
        String cutoffStr = cutoff.toString().replace('T', ' ');

        final String sql = "DELETE FROM sessions WHERE created_at < ?";

        try (PreparedStatement statement = connection.prepareStatement(sql);) {

            statement.setString(1, cutoffStr);

            statement.executeUpdate();

            sessionCache.entrySet()
                    .removeIf(entry -> entry.getValue().getCreatedAt().isBefore(cutoff));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Session findByUserId(Integer id) {

        final String statementString = "SELECT * FROM sessions WHERE user_id=?";

        for (Session cachedSession : sessionCache.values()) {
            if (cachedSession.getUserId() == id) {
                return cachedSession;
            }
        }

        try (PreparedStatement statement = connection.prepareStatement(statementString)) {

            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                Session session = new Session();

                session.setUserId(result.getInt("user_id"));
                session.setToken(result.getString("token"));
                session.setCreatedAt(LocalDateTime.parse(result.getString("created_at")));

                return session;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

}
