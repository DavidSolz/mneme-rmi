package app.apollo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;

public class DBSessionDAO implements SessionDAO{

    private Connection connection;

    public DBSessionDAO(Connection connection)
    {
        this.connection = connection;
    }

    @Override
    public Session findByToken(String token) {
        throw new UnsupportedOperationException("Unimplemented method 'findByToken'");
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

            statement.execute();

        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void deleteExpired(Duration maxAge) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteExpired'");
    }

}
