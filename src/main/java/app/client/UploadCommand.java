package app.client;

import java.util.List;

public class UploadCommand implements Command{

    private String localPath;
    private SessionContext sessionContext;
    
    @Override
    public void execute(FileClient fileClient, AuthClient authClient) {
        fileClient.upload(localPath, sessionContext.getToken());
    }

    @Override
    public void setEvrything(List<String> parameters, SessionContext sessionContext, SessionManager sessionManager) {
        this.localPath = parameters.get(1);
        this.sessionContext = sessionContext;
    }

}
