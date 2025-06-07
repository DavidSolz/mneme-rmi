package app.apollo.client;

import java.util.List;

public class UploadCommand implements Command{

    private String localPath;
    private SessionContext sessionContext;
    
    @Override
    public void execute(FileClient fileClient, AuthClient authClient) {
        if(authClient.validateToken(sessionContext.getToken())){
            fileClient.upload(localPath, sessionContext.getToken());
        }
        else{
            System.out.println("Nie jesteś zalogowany");
        }
    }

    @Override
    public void setEverything(List<String> parameters, SessionContext sessionContext, SessionManager sessionManager) {
        this.localPath = parameters.get(1);
        this.sessionContext = sessionContext;
    }

}
