package app.apollo.client;

public class LogoutCommand implements Command {

    private final Context ctx;

    public LogoutCommand(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void execute(String[] args) throws Exception {

        String token = ctx.session.getToken();

        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("There is no session to end.");
        }

        ctx.authService.logout(token);
    }

    @Override
    public String getName() {
        return "logout";
    }

    @Override
    public String getDescription() {
        return "logout - Ends the session";
    }

}
