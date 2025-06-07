package app.apollo.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SessionManager {
    private String basePath;
    
    public SessionManager(String basePath){
        this.basePath = basePath;
    }
    
    public SessionContext loadFromFile(String fileName){
        SessionContext sessionContext = null;
        Path filePath = Paths.get(basePath + fileName);
        String token = "";
        try {
            if(Files.exists(filePath)){
                token = new String(Files.readAllBytes(filePath), StandardCharsets.US_ASCII);
                sessionContext = new SessionContext();
                sessionContext.setToken(token);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        return sessionContext;
    }
    
    public void saveToFile(String fileName, String token){
        Path filePath = Paths.get(basePath + fileName);
        byte [] data = token.getBytes();
        try {
            if(!Files.exists(Paths.get(basePath))){
                Files.createDirectories(Paths.get(basePath));
            }
            if(!Files.exists(filePath)){
                Files.createFile(filePath);
            }
            Files.write(filePath, data);
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
