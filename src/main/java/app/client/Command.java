package app.client;

public interface Command {
    public void execute(FileClient fileClient, AuthClient AuthClient);
}
