package app.client;

import java.util.List;

public interface Command {
    public void execute(FileClient fileClient, AuthClient AuthClient);
    public void setEverything(List<String> parameters, SessionContext sessionContext, SessionManager sessionManager);
}
