package app.apollo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object (DAO) interface for managing user data.
 * Provides methods for inserting and querying for user data.
 */
public class DBUserDAO implements UserDAO{

    private Connection connection;

    public DBUserDAO(Connection connection)
    {
        this.connection = connection;
    }

    @Override
    public User findByUsername(String username) {
        final String statementString = "SELECT * FROM users WHERE username=?";
        PreparedStatement statement = null;
        User user = null;

        try {
            statement = connection.prepareStatement(statementString);

            statement.setString(1, username);

            ResultSet result = statement.executeQuery();

            if(result.next() == false)
            {
                return null;
            }

            user = new User();

            user.setId(result.getInt("id"));
            user.setUsername(result.getString("username"));
            user.setPassword(result.getString("password"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return user;
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
        User user = null;

        try {
            statement = connection.prepareStatement(statementString);

            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            if(result.next() == false)
            {
                return null;
            }

            user = new User();

            user.setId(result.getInt("id"));
            user.setUsername(result.getString("username"));
            user.setPassword(result.getString("password"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return user;
    }

}
