package app.client;

import java.util.List;

public class DeleteCommand implements Command{
    
    private String fileName;
    private SessionContext sessionContext;
    
    @Override
    public void execute(FileClient fileClient, AuthClient AuthClient) {
        fileClient.delete(fileName, sessionContext.getToken());
    }

    @Override
    public void setEverything(List<String> parameters, SessionContext sessionContext, SessionManager sessionManager) {
        this.fileName = parameters.get(1);
        this.sessionContext = sessionContext;
        
    }

}
