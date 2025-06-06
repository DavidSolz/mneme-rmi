package app.apollo.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import app.apollo.common.AuthService;
import app.apollo.common.Session;

/**
 * Remote implementation of the {@link AuthService} interface.
 * <p>
 * This class delegates authentication operations such as login, logout,
 * registration, and token validation to an internal
 * {@link AuthProviderManager}.
 * </p>
 *
 * <p>
 * Exposed as an RMI service for remote clients.
 * </p>
 */
public class AuthProvider extends UnicastRemoteObject implements AuthService {

    private AuthProviderManager authManager;

    /**
     * Constructs a new {@code AuthProvider} with a given authentication manager.
     *
     * @param authManager The underlying manager that handles actual authentication
     *                    logic.
     * @throws RemoteException If an RMI error occurs during export.
     */
    public AuthProvider(AuthProviderManager authManager) throws RemoteException {
        super();
        this.authManager = authManager;
    }

    @Override
    public boolean register(String username, String password) throws RemoteException {
        boolean result = false;
        result = authManager.register(username, password);
        return result;
    }

    @Override
    public Session login(String username, String password) throws RemoteException {
        Session session = null;
        session = authManager.login(username, password);
        return session;
    }

    @Override
    public void logout(String token) throws RemoteException {
        authManager.logout(token);
    }

    @Override
    public boolean validateToken(String token) throws RemoteException {
        boolean result = false;
        result = authManager.validateToken(token);
        return result;
    }

    @Override
    public Session login(String token) throws RemoteException {
        Session session = null;
        session = authManager.login(token);
        return session;
    }

}
