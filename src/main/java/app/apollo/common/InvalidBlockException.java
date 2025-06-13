package app.apollo.common;

import java.rmi.RemoteException;

public class InvalidBlockException extends RemoteException{

    public InvalidBlockException()
    {
        super();
    }

    public InvalidBlockException(String message)
    {
        super(message);
    }
}
