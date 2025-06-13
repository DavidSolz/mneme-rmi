package app.apollo.client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import app.apollo.common.AuthService;
import app.apollo.common.FileService;

public class App {

    public static void main(String[] args) {

        try {

            String portString = System.getenv("PORT");
            Integer port = portString != null ? Integer.parseInt(portString) : 2567;
            Registry registry = LocateRegistry.getRegistry("localhost", port);

            AuthService authService = (AuthService) registry.lookup("AuthService");
            FileService fileService = (FileService) registry.lookup("FileService");

            Context context = new Context();
            context.authService = authService;
            context.fileService = fileService;

            CommandFactory factory = new CommandFactory(context);

            try (Scanner scanner = new Scanner(System.in)) {

                System.out.println(">> Apollo RMI Client");

                while (true) {
                    System.out.print("> ");
                    String line = scanner.nextLine().trim();
                    if (line.isEmpty()) {
                        continue;
                    }

                    String[] parts = line.split("\\s+");
                    Command command = factory.get(parts[0]);

                    if (command == null) {
                        System.out.println("Unknown command. Type 'help'.");
                        continue;
                    }

                    try {
                        command.execute(parts);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }

                }

            }

        } catch (Exception e) {
            System.out.println("Fatal error: " + e.getMessage());
        }
    }

}
