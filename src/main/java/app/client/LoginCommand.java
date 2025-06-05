package app.client;

public class LoginCommand implements Command{
    private SessionContext sessionContext;
    private String username;
    private String password;
    
    @Override
    public void execute(FileClient fileClient, AuthClient authClient){
        
    }
    
}
