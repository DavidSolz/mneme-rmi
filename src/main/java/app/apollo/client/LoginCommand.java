package app.apollo.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import app.apollo.common.Session;

public class LoginCommand implements Command{
    private SessionManager sessionManager;
    private String username;
    private String password;
    private static String lastLoggedUserFileName;
    
    public static void setLastLoggedUserFileName(String fileName){
        LoginCommand.lastLoggedUserFileName = fileName;
    }
    
    @Override
    public void execute(FileClient fileClient, AuthClient authClient){
        int userId;
        Session session = authClient.login(username, password);
        if(session != null){
                
            String token = session.getToken();
            if(authClient.validateToken(token)){
                System.out.println("Zalogowano");
                userId = session.getUserId();
                fileClient.setClientID(userId);
                sessionManager.saveToFile(String.valueOf(userId), token);
                try {
                    if(!Files.exists(Paths.get(lastLoggedUserFileName))){
                        Files.createFile(Paths.get(lastLoggedUserFileName));
                    }
                    Files.write(Paths.get(lastLoggedUserFileName), String.valueOf(userId).getBytes());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    System.out.println(e.getMessage());
                }
            }
            else{
                System.out.println("Logowanie nieudane");
            }
        }
        else{
            System.out.println("Logowanie nieudane");
        }
    }


    @Override
    public void setEverything(List<String> parameters, SessionContext sessionContext, SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.username = parameters.get(1);
        this.password = parameters.get(2);
    }
    
}
