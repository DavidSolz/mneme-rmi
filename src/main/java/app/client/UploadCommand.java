package app.client;

public class UploadCommand implements Command{

    private String localPath;
    private String remotePath;
    private SessionContext sessionContext;
    
    public UploadCommand(String localPath, String remotePath, SessionContext sessionContext){
        this.localPath = localPath;
        this.remotePath = remotePath;
        this.sessionContext = sessionContext;
    }
    
    @Override
    public void execute(FileClient fileClient, AuthClient AuthClient) {
    }

}
