package app.client;

import java.util.List;

public class LogoutCommand implements Command{

    private SessionContext sessionContext;
    
    
    @Override
    public void execute(FileClient fileClient, AuthClient authClient){
        authClient.logout(sessionContext.getToken());
    }


    @Override
    public void setEvrything(List<String> parameters, SessionContext sessionContext, SessionManager sessionManager) {
        this.sessionContext = sessionContext;
    }
    
}
