package app.client;

public class DeleteCommand implements Command{
    
    private String fileName;
    private SessionContext sessionContext;
    
    public DeleteCommand(String fileName, SessionContext sessionContext){
        this.fileName = fileName;
        this.sessionContext = sessionContext;
    }
    
    @Override
    public void execute(FileClient fileClient, AuthClient AuthClient) {
        fileClient.delete(fileName, sessionContext.getToken());
    }

}
