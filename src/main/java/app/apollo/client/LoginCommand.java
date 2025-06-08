package app.apollo.client;

import app.apollo.common.Session;

public class LoginCommand implements Command {

    private final Context ctx;

    public LoginCommand(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 3) {
            throw new IllegalArgumentException("Usage: login <username> <password>");
        }

        Session session = ctx.authService.login(args[1], args[2]);

        if (session == null) {
            System.out.println("Login failed.");
            return;
        }

        ctx.session = session;
        System.out.println("Login successful.");
    }

    @Override
    public String getName() {
        return "login";
    }

    @Override
    public String getDescription() {
        return "login <username> <password> - Login into service";
    }

}
