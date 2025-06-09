package app.apollo.client;

import java.util.List;

/**
 * Command is an interface that defines the structure for all command classes.
 * Each command must implement methods for execution and parameter initialization.
 */
public interface Command {

    /**
     * Executes the command.
     * This method contains the logic for performing the specific operation of the command.
     * 
     * @param fileClient The FileClient instance for file-related operations.
     * @param authClient The AuthClient instance for authentication operations.
     */
    public void execute(FileClient fileClient, AuthClient authClient);

    /**
     * Sets the necessary context and parameters for the command.
     * This method initializes the command with the required input parameters and session context.
     * 
     * @param parameters     The list of input parameters for the command.
     * @param sessionContext The session context for the current user.
     * @param sessionManager The session manager for managing session data.
     */
    public void setEverything(List<String> parameters, SessionContext sessionContext, SessionManager sessionManager);
}
