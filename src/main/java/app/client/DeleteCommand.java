package app.client;

import java.util.List;

public class DeleteCommand implements Command{
    
    private String fileName;
    private SessionContext sessionContext;
    
    public DeleteCommand(List<String> parameters, SessionContext sessionContext){
        this.fileName = parameters.get(1);
        this.sessionContext = sessionContext;
    }
    
    @Override
    public void execute(FileClient fileClient, AuthClient AuthClient) {
        fileClient.delete(fileName, sessionContext.getToken());
    }

}
