package app.apollo.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import app.apollo.common.AuthService;
import app.apollo.common.FileService;

public final class App {

    public static void main(String[] args) {

        String connectionString = "jdbc:sqlite:data.db";
        DAOFactory factory = null;

        AuthProviderManager authManager = null;
        AuthService authService = null;

        FileProviderManager fileManager = null;
        FileService fileService = null;

        System.setProperty("java.rmi.server.hostname", "localhost");

        try {

            factory = new SQLiteDAOFactory(connectionString);

            authManager = new AuthProviderManager(factory);
            authService = new AuthProvider(authManager);

            fileManager = new FileProviderManager(factory);
            fileService = new FileProvider(authService, fileManager);

            Registry registry = LocateRegistry.createRegistry(2567);
            registry.rebind("AuthService", authService);
            registry.rebind("FileService", fileService);

            System.out.println("RMI server is running...");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
