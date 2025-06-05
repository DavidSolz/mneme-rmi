package app.client;

public class SessionContext {
    private String token;
    
    public void setToken(String token){
        this.token = token;
    }
    public String getToken(){
        return this.token;
    }
}
