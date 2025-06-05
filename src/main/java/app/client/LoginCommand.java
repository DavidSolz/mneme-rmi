package app.client;

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
        sessionContext.setToken(authClient.login(username, password).getToken());
        if(authClient.validateToken(sessionContext.getToken())){
            System.out.println("Zalogowano");
        }
        else{
            System.out.println("Logowanie nieudane");
        }
    }
    
}
