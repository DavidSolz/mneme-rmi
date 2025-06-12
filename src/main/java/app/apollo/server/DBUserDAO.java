package app.apollo.server;

import java.security.cert.PKIXRevocationChecker.Option;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Data Access Object (DAO) interface for managing user data.
 * Provides methods for inserting and querying for user data.
 */
public class DBUserDAO implements UserDAO{

    static final Integer CACHE_CAPACITY = 100;

    private Connection connection;

    private final Cache<Integer, User> userByIdCache;

    private final Cache<String, User> userByNameCache;

    public DBUserDAO(Connection connection)
    {
        this.connection = connection;
        this.userByIdCache = new LRUCache<>(CACHE_CAPACITY);
        this.userByNameCache = new LRUCache<>(CACHE_CAPACITY);
    }

    @Override
    public User findByUsername(String username) {
        final String statementString = "SELECT * FROM users WHERE username=?";
        PreparedStatement statement = null;

        Optional<User> possibleUser = userByNameCache.get(username);

        if(possibleUser.isPresent())
        {
            return possibleUser.get();
        }

        try {
            statement = connection.prepareStatement(statementString);

            statement.setString(1, username);

            ResultSet result = statement.executeQuery();

            if(result.next() == false)
            {
                return null;
            }

            User user = new User();

            user.setId(result.getInt("id"));
            user.setUsername(result.getString("username"));
            user.setPassword(result.getString("password"));

            userByNameCache.put(username, user);

            return user;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    @Override
    public boolean insert(User user) {
        final String statementString = "INSERT INTO users (username, password) VALUES (?, ?)";
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(statementString);

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    @Override
    public User findById(Integer id) {
        final String statementString = "SELECT * FROM users WHERE id=?";
        PreparedStatement statement = null;

        Optional<User> possibleUser = userByIdCache.get(id);

        if(possibleUser.isPresent())
        {
            return possibleUser.get();
        }

        try {
            statement = connection.prepareStatement(statementString);

            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            if(result.next() == false)
            {
                return null;
            }

            User user = new User();

            user.setId(result.getInt("id"));
            user.setUsername(result.getString("username"));
            user.setPassword(result.getString("password"));

            userByIdCache.put(id, user);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

}
