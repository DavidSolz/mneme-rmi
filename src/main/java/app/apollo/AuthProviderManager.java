package app.apollo;

import java.security.InvalidParameterException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import app.common.Session;

public class AuthProviderManager {

    private UserDAO userDAO;
    private SessionDAO sessionDAO;

    public AuthProviderManager(DAOFactory factory) {
        this.userDAO = factory.getUserDAO();
        this.sessionDAO = factory.getSessionDAO();
    }

    public Session login(String username, String password)
    {
        Session session = null;
        User user = null;

        user = userDAO.findByUsername(username);

        if(user == null)
        {
            return null;
        }

        try {

            if( user.getPassword().equals(password) == false )
            {
                throw new InvalidParameterException("Invalid credentials for user '" + username + "'");
            }

            session = sessionDAO.findByUserId(user.getId());

            if( session != null )
            {
                return session;
            }

            String token = UUID.randomUUID().toString();

            session = new Session();

            session.setUserId(user.getId());
            session.setToken(token);
            session.setCreatedAt(LocalDateTime.now());

            sessionDAO.insert(session);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        return session;
    }

    public boolean register(String username, String password)
    {
        User user = null;

        user = new User();

        user.setUsername(username);
        user.setPassword(password);

        boolean result = userDAO.insert(user);

        return result;
    }

    public void logout(String token)
    {
        sessionDAO.delete(token);
    }

    public boolean validateToken(String token)
    {
        sessionDAO.deleteExpired(Duration.ofMinutes(15));
        Session session = sessionDAO.findByToken(token);
        return session != null;
    }

    public Session login(String token) {
        Session session = null;
        session = sessionDAO.findByToken(token);
        return session;
    }

}
