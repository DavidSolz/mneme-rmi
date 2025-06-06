package app.apollo.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;

import app.apollo.common.Session;

/**
 * Data Access Object (DAO) implementation for accessing and manipulating
 * User records in the database.
 */
public class DBSessionDAO implements SessionDAO {

    private Connection connection;

    /**
     * Constructs a new DBUserDAO with the given database connection.
     *
     * @param connection the active database connection
     */
    public DBSessionDAO(Connection connection) {
        this.connection = connection;
        clearAllSessions();
    }

    private void clearAllSessions() {
        final String sql = "DELETE FROM sessions";
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(sql);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Failed to clear sessions: " + e.getMessage());
        }
    }

    @Override
    public Session findByToken(String token) {
        final String statementString = "SELECT * FROM sessions WHERE token=?";
        PreparedStatement statement = null;
        Session session = null;

        try {
            statement = connection.prepareStatement(statementString);

            statement.setString(1, token);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                session = new Session();

                session.setUserId(result.getInt("user_id"));
                session.setToken(result.getString("token"));
                session.setCreatedAt(LocalDateTime.parse(result.getString("created_at")));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return session;
    }

    @Override
    public boolean insert(Session session) {
        final String statementString = "INSERT INTO sessions (user_id, token, created_at) VALUES (?, ?, ?)";
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(statementString);

            statement.setInt(1, session.getUserId());
            statement.setString(2, session.getToken());
            statement.setString(3, session.getCreatedAt().toString());

            return statement.execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    @Override
    public void delete(String token) {

        final String statementString = "DELETE FROM sessions WHERE token = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(statementString);

            statement.setString(1, token);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void deleteExpired(Duration maxAge) {
        LocalDateTime cutoff = LocalDateTime.now().minus(maxAge);
        String cutoffStr = cutoff.toString().replace('T', ' ');

        final String sql = "DELETE FROM sessions WHERE created_at < ?";
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(sql);

            statement.setString(1, cutoffStr);

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Session findByUserId(Integer id) {

        final String statementString = "SELECT * FROM sessions WHERE user_id=?";
        PreparedStatement statement = null;
        Session session = null;

        try {
            statement = connection.prepareStatement(statementString);

            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                session = new Session();

                session.setUserId(result.getInt("user_id"));
                session.setToken(result.getString("token"));
                session.setCreatedAt(LocalDateTime.parse(result.getString("created_at")));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return session;
    }

}
