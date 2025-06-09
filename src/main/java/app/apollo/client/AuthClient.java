package app.apollo.client;

import java.rmi.RemoteException;

import app.apollo.common.AuthService;
import app.apollo.common.Session;

public class AuthClient {
    private AuthService authService;
    
    public AuthClient(AuthService authService){
        this.authService = authService;
    }
    
    public Session login(String username, String password){
        try {
            return authService.login(username, password);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    public boolean register(String username, String password){
        try {
            return authService.register(username, password);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        }
        return false;
    }
    
    public void logout(String token){
        try {
            authService.login(token);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        }
    }
    
    public boolean validateToken(String token){
        try {
            return authService.validateToken(token);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        }
        return false;
    }
    
    public Session login(String token){
        try {
            return authService.login(token);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        }
        return null;
    }
    
}