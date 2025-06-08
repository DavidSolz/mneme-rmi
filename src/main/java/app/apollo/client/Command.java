package app.apollo.client;

public interface Command {

    void execute(String[] args) throws Exception;

    String getName();

    String getDescription();

}
