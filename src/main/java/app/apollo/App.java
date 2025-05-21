package app.apollo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import app.common.AuthService;

public final class App {

    public static void main(String[] args) {

        String connectionString = "jdbc:sqlite:data.db";
        DAOFactory factory = null;

        AuthProviderManager authManager = null;
        AuthService service = null;

        try {

            factory = new SQLiteDAOFactory(connectionString);

            authManager = new AuthProviderManager(factory);
            service = new AuthProvider(authManager);

            Registry registry = LocateRegistry.createRegistry(2567);
            registry.rebind("AuthService", service);

            System.out.println("RMI server is running...");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
