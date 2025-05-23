package app.apollo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import app.common.AuthService;
import app.common.FileService;

public class FileProvider extends UnicastRemoteObject implements FileService {

    static final Long BLOCK_SIZE = (long) 1024;

    private AuthService authService;
    private FileProviderManager fileManager;

    public FileProvider(AuthService authService, FileProviderManager fileManager) throws RemoteException {
        this.authService = authService;
        this.fileManager = fileManager;
    }

    @Override
    public void uploadBlock(String token, String filename, byte[] data, long size, long blockIndex) throws RemoteException {

        if ( authService.validateToken(token) == false )
        {
            throw new RemoteException("Token expired");
        }

        Integer userId = authService.retrieveUserIdFromToken(token);

        fileManager.uploadBlock(userId, filename, data, size, blockIndex);
        // TODO

    }

    @Override
    public void deleteFile(String token, String filename) throws RemoteException {

        if ( authService.validateToken(token) == false )
        {
            throw new RemoteException("Token expired");
        }

        Integer userId = authService.retrieveUserIdFromToken(token);

        fileManager.deleteFile(userId, filename);

        // TODO

    }

    @Override
    public List<String> getChecksums(String token, String filename) throws RemoteException {

        List<String> checksums = null;

        if ( authService.validateToken(token) == false )
        {
            throw new RemoteException("Token expired");
        }

        Integer userId = authService.retrieveUserIdFromToken(token);

        checksums = fileManager.getChecksums(userId, filename);

        return checksums;
    }

    @Override
    public byte[] downloadBlock(String token, String filename, long blockIndex) throws RemoteException {

        byte[] block = null;

        if ( authService.validateToken(token) == false )
        {
            throw new RemoteException("Token expired");
        }

        Integer userId = authService.retrieveUserIdFromToken(token);

        block = fileManager.downloadBlock(userId, filename, blockIndex);

        return block;
    }

    @Override
    public List<String> listFiles(String token) throws RemoteException {

        List<String> filenames = null;

        if ( authService.validateToken(token) == false )
        {
            throw new RemoteException("Token expired");
        }

        // TODO

        return filenames;
    }

    @Override
    public long getBlockSize() throws RemoteException {
        return BLOCK_SIZE;
    }

}
