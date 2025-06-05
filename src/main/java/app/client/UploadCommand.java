package app.client;

public class UploadCommand implements Command{

    private String localPath;
    private SessionContext sessionContext;
    
    public UploadCommand(String localPath, SessionContext sessionContext){
        this.localPath = localPath;
        this.sessionContext = sessionContext;
    }
    
    @Override
    public void execute(FileClient fileClient, AuthClient authClient) {
        fileClient.upload(localPath, sessionContext.getToken());
    }

}
