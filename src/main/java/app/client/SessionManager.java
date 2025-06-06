package app.client;

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
        SessionContext sessionContext = new SessionContext();
        Path filePath = Paths.get(basePath + fileName);
        String token = "";
        try {
            token = new String(Files.readAllBytes(filePath), StandardCharsets.US_ASCII);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        sessionContext.setToken(token);
        
        return sessionContext;
    }
    
    public void saveToFile(String fileName, String token){
        Path filePath = Paths.get(basePath + fileName);
        byte [] data = token.getBytes();
        try {
            Files.write(filePath, data);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
