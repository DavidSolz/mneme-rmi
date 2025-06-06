package app.client;

import java.util.List;

public class LogoutCommand implements Command{

    private SessionContext sessionContext;
    
    public LogoutCommand(List<String> parameters, SessionContext sessionContext){
        this.sessionContext = sessionContext;
    }
    
    @Override
    public void execute(FileClient fileClient, AuthClient authClient){
        authClient.logout(sessionContext.getToken());
    }
    
}
