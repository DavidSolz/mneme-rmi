package app.apollo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDAOFactory implements DAOFactory{

    private Connection connection;

    public SQLiteDAOFactory(String url)
    {
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public SessionDAO getSessionDAO() {
        return new DBSessionDAO(connection);
    }

}
