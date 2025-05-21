package app.apollo;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.UUID;

public class AuthProviderManager {

    private UserDAO userDAO;
    private SessionDAO sessionDAO;

    public AuthProviderManager(DAOFactory factory) {
        this.userDAO = factory.getUserDAO();
        this.sessionDAO = factory.getSessionDAO();
    }

    public String login(String username, String password)
    {
        String token = "";
        User user = null;
        Session session = null;

        user = userDAO.findByUsername(username);

        try {

            if( user.getPassword().equals(password) == false )
            {
                throw new InvalidParameterException("Invalid credentials for user '" + username + "'");
            }

            session = sessionDAO.findByUserId(user.getId());

            if( session != null )
            {
                return session.getToken();
            }

            token = UUID.randomUUID().toString();

            session = new Session();

            session.setUserId(user.getId());
            session.setToken(token);
            session.setCreatedAt(LocalDateTime.now());

            sessionDAO.insert(session);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return token;
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
        Session session = sessionDAO.findByToken(token);
        return session != null;
    }

}
