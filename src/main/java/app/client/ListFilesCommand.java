package app.client;

import java.util.List;


public class ListFilesCommand implements Command{
    
    private SessionContext sessionContext;
    
    public ListFilesCommand(SessionContext sessionContext){
        this.sessionContext = sessionContext;
    }
    
    @Override
    public void execute(FileClient fileClient, AuthClient authClient){
        List<String> fileList;
        
        fileList = fileClient.listFiles(sessionContext.getToken());
        
        System.out.println("Files list:\n");
        
        for(String i:fileList){
            System.out.println(i);
        }
        
        
    }

}
