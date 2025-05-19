package app.apollo;

import java.time.Duration;

public interface SessionDAO {

    public Session findByToken(String token);
    public boolean insert(Session session);
    public void delete(String token);
    public void deleteExpired(Duration maxAge);
}
