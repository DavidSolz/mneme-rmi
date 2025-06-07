package app.apollo.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import app.apollo.common.AuthService;
import app.apollo.common.FileService;

public class App {

    public static void main(String[] args) throws RemoteException, NotBoundException {
        /*
         * ======== HOW TO CONNECT TO SERVICE ========
         *
         * String portString = System.getenv("PORT");
         * Integer port = portString != null ? Integer.parseInt(portString) : 2567;
         * Registry registry = LocateRegistry.getRegistry("localhost", port);
         *
         * AuthService authService = (AuthService) registry.lookup("AuthService");
         * FileService fileService = (FileService) registry.lookup("FileService");
         */
        String lastLogedUserIDFileName = "lastLogged";
        FileClient.setLastLoggedUserFileName(lastLogedUserIDFileName);
        LoginCommand.setLastLoggedUserFileName(lastLogedUserIDFileName);
        
        String portString = System.getenv("PORT");
        Integer port = portString != null ? Integer.parseInt(portString) : 2567;
        Registry registry = LocateRegistry.getRegistry("localhost", port);
        AuthService authService = (AuthService) registry.lookup("AuthService");
        FileService fileService = (FileService) registry.lookup("FileService");
        String basePath = "tokens/";
        SessionManager sessionManager = new SessionManager(basePath);
        CommandFactory commandFactory = new CommandFactory(sessionManager);
        FileClient fileClient = new FileClient(fileService);
        AuthClient authClient = new AuthClient(authService);
        Scanner scanner = new Scanner(System.in);
        String input;
        Command command;
        LogoutCommand.setBasePath(basePath);
        
        while(true){
            input = scanner.nextLine();
            command = commandFactory.createCommand(input, String.valueOf(fileClient.getClientID()));
            if(command != null){
                command.execute(fileClient, authClient);
            }
        }
        
    }

}
