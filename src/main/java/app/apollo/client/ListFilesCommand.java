package app.apollo.client;

import java.util.List;


public class ListFilesCommand implements Command{
    
    private SessionContext sessionContext;
    
    @Override
    public void execute(FileClient fileClient, AuthClient authClient){
        if(authClient.validateToken(sessionContext.getToken())){
            
            List<String> fileList;
            
            fileList = fileClient.listFiles(sessionContext.getToken());
            
            System.out.println("Files list:");
            
            for(String i:fileList){
                System.out.println("- " + i);
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
