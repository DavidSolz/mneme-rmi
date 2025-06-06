package app.client;

import java.util.List;

public interface Command {
    public void execute(FileClient fileClient, AuthClient AuthClient);
    public void setEvrything(List<String> parameters, SessionContext sessionContext, SessionManager sessionManager);
}
