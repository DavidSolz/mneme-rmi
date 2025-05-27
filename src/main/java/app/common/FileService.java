package app.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface FileService extends Remote{

    public void uploadBlock(String token, String filename, Block block) throws RemoteException;

    public void deleteFile(String token, String filename) throws RemoteException;

    public List<String> getChecksums(String token, String filename) throws RemoteException;

    public Block downloadBlock(String token, String filename, long blockIndex) throws RemoteException;

    public List<String> listFiles(String token) throws RemoteException;

    public long getBlockSize() throws RemoteException;
}
