package app.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

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

    }

}
