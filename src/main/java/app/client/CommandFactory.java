package app.client;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CommandFactory {
    private SessionManager sessionManager;
    private Map<String, Class<? extends Command>> classMap;
    
    public CommandFactory(SessionManager sessionManager){
        this.sessionManager = sessionManager;
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
        
        List<String> parameters = Arrays.asList(input.split(" "));
        classMap.get(parameters.get(0));
        try {
            command = classMap.get(parameters.get(0)).getDeclaredConstructor().newInstance(parameters, sessionManager);
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
        
        
        return command;
    }
}
