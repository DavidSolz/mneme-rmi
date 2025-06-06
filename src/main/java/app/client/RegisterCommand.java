package app.client;

import java.util.List;

public class RegisterCommand implements Command{
    private String userName;
    private String password;
    
    
    public RegisterCommand(List<String> parameters){
        this.userName = parameters.get(1);
        this.password = parameters.get(2);
    }
    
    @Override
    public void execute(FileClient fileClient, AuthClient authClient){
        if(authClient.register(userName, password)){
            System.out.println("Rejestracja zakończona powodzeniem");
        }
        else{
            System.out.println("Rejestracja zakończona niepowodzeniem");
        }
    }
}
