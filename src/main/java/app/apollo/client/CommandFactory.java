package app.apollo.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandFactory {

    private final Map<String, Command> commands = new HashMap<>();

    public CommandFactory(Context context) throws Exception {
        register(new HelpCommand(this));
        register(new RegisterCommand(context));
        register(new LoginCommand(context));
        register(new LogoutCommand(context));
        register(new UploadCommand(context));
        register(new DownloadCommand(context));
        register(new DeleteCommand(context));
        register(new ListCommand(context));
        register(new ClearCommand());
    }

    public void register(Command cmd) throws Exception {
        String commandName = cmd.getName();

        if (commands.containsKey(commandName)) {
            throw new Exception("Command '" + commandName + "' already bound.");
        }

        commands.put(commandName, cmd);
    }

    public Command get(String name) {
        return commands.get(name);
    }

    public List<Command> getAll() {
        return commands.entrySet()
                .stream()
                .map(x -> x.getValue())
                .collect(Collectors.toList());
    }

}
