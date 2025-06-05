package app.client;

import app.common.Session;

public class LoginCommand implements Command{
    private SessionContext sessionContext;
    private String username;
    private String password;
    
    public LoginCommand(SessionContext sessionContext, String userName, String password){
        this.sessionContext = sessionContext;
        this.username = userName;
        this.password = password;
    }
    
    @Override
    public void execute(FileClient fileClient, AuthClient authClient){
        Session session = authClient.login(username, password);
        sessionContext.setToken(session.getToken());
        if(authClient.validateToken(sessionContext.getToken())){
            System.out.println("Zalogowano");
            fileClient.setClientID(session.getUserId());
        }
        else{
            System.out.println("Logowanie nieudane");
        }
    }
    
}
