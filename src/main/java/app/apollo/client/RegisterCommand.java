package app.apollo.client;

public class RegisterCommand implements Command {

    private final Context ctx;

    public RegisterCommand(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 3) {
            throw new IllegalArgumentException("Usage: register <username> <password>");
        }
        boolean ok = ctx.authService.register(args[1].trim(), args[2].trim());
        System.out.println(ok ? "Registration successful." : "Registration failed.");
    }

    @Override
    public String getName() {
        return "register";
    }

    @Override
    public String getDescription() {
        return "register <username> <password> - Register a new user";
    }

}
