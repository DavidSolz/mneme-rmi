package app.apollo.client;

import java.util.List;

public class DeleteCommand implements Command{
    
    private String fileName;
    private SessionContext sessionContext;
    
    @Override
    public void execute(FileClient fileClient, AuthClient authClient) {
        if(authClient.validateToken(sessionContext.getToken())){
            fileClient.delete(fileName, sessionContext.getToken());
        }
        else{
            System.out.println("Nie jesteś zalogowany");
        }
    }

    @Override
    public void setEverything(List<String> parameters, SessionContext sessionContext, SessionManager sessionManager) {
        this.fileName = parameters.get(1);
        this.sessionContext = sessionContext;
        
    }

}
