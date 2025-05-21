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
        Registry registry = null;

        try {

            factory = new SQLiteDAOFactory(connectionString);

            authManager = new AuthProviderManager(factory);
            service = new AuthProvider(authManager);

            registry = LocateRegistry.createRegistry(2567);
            registry.bind("AuthService", service);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
