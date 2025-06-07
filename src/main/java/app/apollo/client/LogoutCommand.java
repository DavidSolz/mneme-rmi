package app.apollo.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class LogoutCommand implements Command{

    private SessionContext sessionContext;
    private static String basePath;
    
    public static void setBasePath(String basePath) {
        LogoutCommand.basePath = basePath;
    }
    
    @Override
    public void execute(FileClient fileClient, AuthClient authClient){
        if(authClient.validateToken(sessionContext.getToken())){
            
            authClient.logout(sessionContext.getToken());
            String userID = String.valueOf(fileClient.getClientID());
            Path filename = Paths.get(basePath, userID);
            if(Files.exists(filename)){
                try {
                    Files.write(filename, "Invalid Token".getBytes());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        else{
            System.out.println("Nie jesteś zalogowany");
        }
    }


    @Override
    public void setEverything(List<String> parameters, SessionContext sessionContext, SessionManager sessionManager) {
        this.sessionContext = sessionContext;
    }
    
}
