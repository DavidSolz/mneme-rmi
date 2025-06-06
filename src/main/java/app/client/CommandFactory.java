package app.client;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandFactory {
    private SessionManager sessionManager;
    private Map<String, Class<? extends Command>> classMap;
    
    public CommandFactory(SessionManager sessionManager){
        this.sessionManager = sessionManager;
        classMap = new HashMap<>();
        classMap.put("ListFiles", ListFilesCommand.class);
        classMap.put("Logout", LogoutCommand.class);
        classMap.put("Upload", UploadCommand.class);
        classMap.put("Download", DownloadCommand.class);
        classMap.put("Delete", DeleteCommand.class);
        classMap.put("Login", LoginCommand.class);
        classMap.put("Register", RegisterCommand.class);
    }
    
    public Command createCommand(String input, String userID){
        Command command = null;
        SessionContext sessionContext;
        List<String> parameters = Arrays.asList(input.split(" "));
        if(checkUserInput(parameters)){
            try {
                // command = classMap.get(parameters.get(0)).getDeclaredConstructor().newInstance(parameters, sessionManager);  
                command = classMap.get(parameters.get(0)).getConstructor().newInstance();
                sessionContext = sessionManager.loadFromFile(userID);
                // if(sessionContext != null){
                command.setEvrything(parameters, sessionManager.loadFromFile(userID), sessionManager);
                    
                // }
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            };
        }
        else{
            System.out.println("Błędna instrukcja");
        }
        
        return command;
    }
    
    private boolean checkUserInput(List<String> parameters){
        
        
        if(parameters.size() == 0){
            return false;
        }
        
        if(parameters.get(0).equals("ListFiles")){
            if(parameters.size() != 1){
                return false;
            }
        }
        else if(parameters.get(0).equals("Logout")){
            if(parameters.size() != 1){
                return false;
            }
            
        }
        else if(parameters.get(0).equals("Upload")){
            if(parameters.size() != 2){
                return false;
            }
        }
        else if(parameters.get(0).equals("Download")){
            if(parameters.size() != 3){
                return false;
            }
        }
        else if(parameters.get(0).equals("Delete")){
            if(parameters.size() != 2){
                return false;
            }
        }
        else if(parameters.get(0).equals("Login")){
            if(parameters.size() != 3){
                return false;
            }
        }
        else if(parameters.get(0).equals("Register")){
            if(parameters.size() != 3){
                return false;
            }
        }
        else{
            return false;
        }
        
        return true;
    }
    
}
