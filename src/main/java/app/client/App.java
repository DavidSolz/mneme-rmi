package app.client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import app.common.AuthService;

public class App {

    public static void main(String[] args) {

        AuthService service = null;

        try {
            String portString = System.getenv("PORT");
            Integer port = Integer.parseInt(portString);
            Registry registry = LocateRegistry.getRegistry(port);
            service = (AuthService) registry.lookup("AuthService");

            boolean result = service.register("admin", "admin");

            if( result == false )
            {
                System.out.println("Failed to create accout with this credentials.");
            }

            String token = service.login("admin", "admin");

            System.out.println("Token: " + token);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

}
