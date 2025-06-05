package app.client;

public class LogoutCommand implements Command{

    private SessionContext sessionContext;
    
    public LogoutCommand(SessionContext sessionContext){
        this.sessionContext = sessionContext;
    }
    
    @Override
    public void execute(FileClient fileClient, AuthClient authClient){
        authClient.logout(sessionContext.getToken());
    }
    
}
