package app.apollo;

import java.rmi.RemoteException;

import app.common.AuthService;

public class AuthProvider implements AuthService
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
    public String login(String username, String password) throws RemoteException{
        String token = "";
        token = authManager.login(username, password);
        return token;
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

}
