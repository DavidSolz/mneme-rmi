package app.apollo.client;

public class HelpCommand implements Command {

    private final CommandFactory factory;

    public HelpCommand(CommandFactory factory) {
        this.factory = factory;
    }

    @Override
    public void execute(String[] args) throws Exception {
        System.out.println("Available commands:");
        factory.getAll()
                .forEach(c -> System.out.println("  " + c.getDescription()));
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "help - Show list of commands";
    }

}
