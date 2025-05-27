package app.common;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Session implements Serializable{

    private String token;
    private Integer userId;
    private LocalDateTime createdAt;

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime date)
    {
        this.createdAt = date;
    }


}
