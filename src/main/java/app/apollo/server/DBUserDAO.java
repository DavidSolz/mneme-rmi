package app.apollo.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Data Access Object (DAO) interface for managing user data.
 * Provides methods for inserting and querying for user data.
 */
public class DBUserDAO implements UserDAO {

    static final Integer CACHE_CAPACITY = 100;

    private Connection connection;

    private final Cache<String, User> userCache;

    public DBUserDAO(Connection connection) {
        this.connection = connection;
        this.userCache = new LRUCache<>(CACHE_CAPACITY);
    }

    @Override
    public User findByUsername(String username) {

        Optional<User> possibleUser = userCache.get(username);

        if (possibleUser.isPresent()) {
            return possibleUser.get();
        }

        final String statementString = "SELECT * FROM users WHERE username=?";

        try (PreparedStatement statement = connection.prepareStatement(statementString)) {

            statement.setString(1, username);

            ResultSet result = statement.executeQuery();

            if (result.next() == false) {
                return null;
            }

            User user = new User();

            user.setId(result.getInt("id"));
            user.setUsername(result.getString("username"));
            user.setPassword(result.getString("password"));

            userCache.put(username, user);

            return user;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    @Override
    public boolean insert(User user) {
        final String statementString = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(statementString)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());

            boolean insterted = statement.executeUpdate() > 0;

            if (insterted) {
                userCache.put(user.getUsername(), user);
            }

            return insterted;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    @Override
    public User findById(Integer id) {

        for (User user : userCache.values()) {
            if (user.getId() == id) {
                return user;
            }
        }

        final String statementString = "SELECT * FROM users WHERE id=?";

        try (PreparedStatement statement = connection.prepareStatement(statementString)) {

            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            if (result.next() == false) {
                return null;
            }

            User user = new User();

            user.setId(result.getInt("id"));
            user.setUsername(result.getString("username"));
            user.setPassword(result.getString("password"));

            userCache.put(user.getUsername(), user);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

}
