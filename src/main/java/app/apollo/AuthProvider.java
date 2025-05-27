package app.apollo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import app.common.AuthService;
import app.common.Session;

public class AuthProvider extends UnicastRemoteObject implements AuthService
{

    private AuthProviderManager authManager;

    public AuthProvider(AuthProviderManager authManager) throws RemoteException
    {
        this.authManager = authManager;
    }

    @Override
    public boolean register(String username, String password) throws RemoteException{
        boolean result = false;
        result = authManager.register(username, password);
        return result;
    }

    @Override
    public Session login(String username, String password) throws RemoteException{
        Session session = null;
        session = authManager.login(username, password);
        return session;
    }

    @Override
    public void logout(String token) throws RemoteException{
        authManager.logout(token);
    }

    @Override
    public boolean validateToken(String token) throws RemoteException{
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
