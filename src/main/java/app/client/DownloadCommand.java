package app.client;

public class DownloadCommand implements Command{
    private String localPath;
    private String fileName;
    private SessionContext sessionContext;
    
    public DownloadCommand(String localPath, String fileName, SessionContext sessionContext){
        this.localPath = localPath;
        this.fileName = fileName;
        this.sessionContext = sessionContext;
    }

    @Override
    public void execute(FileClient fileClient, AuthClient authClient) {
        fileClient.download(fileName, localPath, sessionContext.getToken());
    }
    
    
}
