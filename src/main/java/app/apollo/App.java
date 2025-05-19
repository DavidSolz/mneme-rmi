package app.apollo;

import java.time.LocalDateTime;

public final class App {

    public static void main(String[] args) {

        String connectionString = "jdbc:sqlite:data.db";
        DAOFactory factory = new SQLiteDAOFactory(connectionString);
        SessionDAO sessionDAO = factory.getSessionDAO();

        Session session = new Session();
        session.setCreatedAt(LocalDateTime.now());
        session.setToken("1234");
        session.setUserId(123456);

        sessionDAO.insert(session);

        sessionDAO.delete("12345");

    }
}
