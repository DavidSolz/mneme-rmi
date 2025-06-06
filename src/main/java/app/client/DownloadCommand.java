package app.client;

import java.util.List;

public class DownloadCommand implements Command{
    private String localPath;
    private String fileName;
    private SessionContext sessionContext;
    

    @Override
    public void execute(FileClient fileClient, AuthClient authClient) {
        fileClient.download(fileName, localPath, sessionContext.getToken());
    }


    @Override
    public void setEverything(List<String> parameters, SessionContext sessionContext, SessionManager sessionManager) {
        this.localPath = parameters.get(1);
        this.fileName = parameters.get(2);
        this.sessionContext = sessionContext;
    }
    
    
}
