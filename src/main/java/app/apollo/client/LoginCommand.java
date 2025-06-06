package app.apollo.client;

import java.util.List;

import app.apollo.common.Session;

public class LoginCommand implements Command{
    private SessionManager sessionManager;
    private String username;
    private String password;
    
    
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


    @Override
    public void setEverything(List<String> parameters, SessionContext sessionContext, SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.username = parameters.get(1);
        this.password = parameters.get(2);
    }
    
}
