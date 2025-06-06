package app.client;

import java.util.List;

import app.common.Session;

public class LoginCommand implements Command{
    private SessionManager sessionManager;
    private String username;
    private String password;
    
    public LoginCommand(List<String> parameters, SessionManager sessionManager){
        this.sessionManager = sessionManager;
        this.username = parameters.get(1);
        this.password = parameters.get(2);
    }
    
    @Override
    public void execute(FileClient fileClient, AuthClient authClient){
        int userId;
        Session session = authClient.login(username, password);
        String token = session.getToken();
        if(authClient.validateToken(token)){
            System.out.println("Zalogowano");
            userId = session.getUserId();
            fileClient.setClientID(userId);
            sessionManager.saveToFile(String.valueOf(userId), token);
        }
        else{
            System.out.println("Logowanie nieudane");
        }
    }
    
}
