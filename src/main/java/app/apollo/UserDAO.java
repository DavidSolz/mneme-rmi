package app.apollo;

public interface UserDAO {

    public User findByUsername(String username);
    public boolean insert(User user);
    public User findById(Integer id);
}
