package app.client;

import java.util.List;

public class DownloadCommand implements Command{
    private String localPath;
    private String fileName;
    private SessionContext sessionContext;
    
    public DownloadCommand(List<String> parameters, SessionContext sessionContext){
        this.localPath = parameters.get(1);
        this.fileName = parameters.get(2);
        this.sessionContext = sessionContext;
    }

    @Override
    public void execute(FileClient fileClient, AuthClient authClient) {
        fileClient.download(fileName, localPath, sessionContext.getToken());
    }
    
    
}
