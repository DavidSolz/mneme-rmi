package app.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuthService extends Remote {

    public boolean register(String username, String password) throws RemoteException;

    public String login(String username, String password) throws RemoteException;

    public void logout(String token) throws RemoteException;

    public boolean validateToken(String token) throws RemoteException;
}
