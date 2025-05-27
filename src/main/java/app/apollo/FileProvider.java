package app.apollo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import app.common.AuthService;
import app.common.Block;
import app.common.FileService;
import app.common.Session;

public class FileProvider extends UnicastRemoteObject implements FileService {

    static final Long BLOCK_SIZE = (long) 1024;

    private AuthService authService;
    private FileProviderManager fileManager;

    public FileProvider(AuthService authService, FileProviderManager fileManager) throws RemoteException {
        this.authService = authService;
        this.fileManager = fileManager;
    }

    @Override
    public void uploadBlock(String token, String filename, Block block) throws RemoteException {

        if ( authService.validateToken(token) == false )
        {
            throw new RemoteException("Token expired");
        }

        Session session = authService.login(token);

        fileManager.uploadBlock(session.getUserId(), filename, block);

    }

    @Override
    public void deleteFile(String token, String filename) throws RemoteException {

        if ( authService.validateToken(token) == false )
        {
            throw new RemoteException("Token expired");
        }

        Session session = authService.login(token);

        fileManager.deleteFile(session.getUserId(), filename);

    }

    @Override
    public List<String> getChecksums(String token, String filename) throws RemoteException {

        List<String> checksums = null;

        if ( authService.validateToken(token) == false )
        {
            throw new RemoteException("Token expired");
        }

        Session session = authService.login(token);

        checksums = fileManager.getChecksums(session.getUserId(), filename);

        return checksums;
    }

    @Override
    public Block downloadBlock(String token, String filename, long blockIndex) throws RemoteException {

        Block block = null;

        if ( authService.validateToken(token) == false )
        {
            throw new RemoteException("Token expired");
        }

        Session session = authService.login(token);

        block = fileManager.downloadBlock(session.getUserId(), filename, blockIndex);

        return block;
    }

    @Override
    public List<String> listFiles(String token) throws RemoteException {

        List<String> filenames = null;

        if ( authService.validateToken(token) == false )
        {
            throw new RemoteException("Token expired");
        }

        Session session = authService.login(token);

        filenames = fileManager.listFiles(session.getUserId());

        return filenames;
    }

    @Override
    public long getBlockSize() throws RemoteException {
        return BLOCK_SIZE;
    }

}
