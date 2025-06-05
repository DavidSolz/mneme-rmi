package app.client;

public class RegisterCommand implements Command{
    private String userName;
    private String password;
    
    
    public RegisterCommand(String userName, String password){
        this.userName = userName;
        this.password = password;
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
