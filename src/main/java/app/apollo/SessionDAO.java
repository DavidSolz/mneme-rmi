package app.apollo;

import java.time.Duration;

import app.common.Session;

public interface SessionDAO {

    public Session findByToken(String token);
    public Session findByUserId(Integer id);
    public boolean insert(Session session);
    public void delete(String token);
    public void deleteExpired(Duration maxAge);
}
