package app.apollo.client;

import java.util.List;

public class DownloadCommand implements Command{
    private String localPath;
    private String fileName;
    private SessionContext sessionContext;
    

    @Override
    public void execute(FileClient fileClient, AuthClient authClient) {
        if(authClient.validateToken(sessionContext.getToken())){
            fileClient.download(fileName, localPath, sessionContext.getToken());
        }
        else{
            System.out.println("Nie jesteś zalogowany");
        }
    }


    @Override
    public void setEverything(List<String> parameters, SessionContext sessionContext, SessionManager sessionManager) {
        this.localPath = parameters.get(1);
        this.fileName = parameters.get(2);
        this.sessionContext = sessionContext;
    }
    
    
}
