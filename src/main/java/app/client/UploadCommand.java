package app.client;

import java.util.List;

public class UploadCommand implements Command{

    private String localPath;
    private SessionContext sessionContext;
    
    public UploadCommand(List<String> parameters, SessionContext sessionContext){
        this.localPath = parameters.get(1);
        this.sessionContext = sessionContext;
    }
    
    @Override
    public void execute(FileClient fileClient, AuthClient authClient) {
        fileClient.upload(localPath, sessionContext.getToken());
    }

}
