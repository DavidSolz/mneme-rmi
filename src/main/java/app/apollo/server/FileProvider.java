package app.apollo.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import app.apollo.common.AuthService;
import app.apollo.common.Block;
import app.apollo.common.FileService;
import app.apollo.common.FrozenPair;
import app.apollo.common.InvalidBlockException;
import app.apollo.common.Session;

/**
 * Remote implementation of the {@link FileService} interface for file
 * operations.
 * <p>
 * This class handles client requests for file block storage, retrieval,
 * and metadata management. It delegates business logic to a
 * {@link FileProviderManager},
 * and uses {@link AuthService} to authenticate users.
 * </p>
 *
 * <p>
 * Exposed as an RMI service to support distributed file systems.
 * </p>
 */
public class FileProvider extends UnicastRemoteObject implements FileService {

    /** Fixed block size used across all file operations. */
    static final Integer BLOCK_SIZE = 1024;

    /** Service responsible for authorization managing */
    private AuthService authService;

    /** Interface that is responsible for block validation */
    private BlockValidator blockValidator;

    /** Service respnsible for file managing */
    private FileProviderManager fileManager;

    /**
     * Constructs a new FileProvider service.
     *
     * @param authService The authentication service for validating user tokens.
     * @param fileManager The manager handling the actual file storage operations.
     * @throws RemoteException If an RMI error occurs during initialization.
     */
    public FileProvider(AuthService authService, FileProviderManager fileManager) throws RemoteException {
        super();
        this.authService = authService;
        this.fileManager = fileManager;
        this.blockValidator = new DataBlockValidator(BLOCK_SIZE);
    }

    @Override
    public void setFileBlockCount(String token, String filename, long numBlocks) throws RemoteException {

        if (authService.validateToken(token) == false) {
            throw new RemoteException("Token expired");
        }

        Session session = authService.login(token);

        fileManager.setFileBlockCount(session.getUserId(), filename, numBlocks);
    }

    @Override
    public long getFileBlockCount(String token, String filename) throws RemoteException {

        if (authService.validateToken(token) == false) {
            throw new RemoteException("Token expired");
        }

        Session session = authService.login(token);

        return fileManager.getFileBlockCount(session.getUserId(), filename);
    }

    @Override
    public void uploadBlock(String token, String filename, Block block) throws RemoteException {

        if (authService.validateToken(token) == false) {
            throw new RemoteException("Token expired");
        }

        Session session = authService.login(token);

        if(!blockValidator.isBlockValid(block))
        {
            throw new InvalidBlockException("Block " + block.getSequenceNumber() + " in file: `" + filename + "` is invalid.");
        }

        fileManager.uploadBlock(session.getUserId(), filename, block);

    }

    @Override
    public void deleteFile(String token, String filename) throws RemoteException {

        if (authService.validateToken(token) == false) {
            throw new RemoteException("Token expired");
        }

        Session session = authService.login(token);

        fileManager.deleteFile(session.getUserId(), filename);

    }

    @Override
    public List<FrozenPair<String,String>> getChecksums(String token, String filename) throws RemoteException {

        if (authService.validateToken(token) == false) {
            throw new RemoteException("Token expired");
        }

        Session session = authService.login(token);

        List<FrozenPair<String,String>> checksums = fileManager.getChecksums(session.getUserId(), filename);

        return checksums;
    }

    @Override
    public Block downloadBlock(String token, String filename, long blockIndex) throws RemoteException {

        if (authService.validateToken(token) == false) {
            throw new RemoteException("Token expired");
        }

        Session session = authService.login(token);

        Block block = fileManager.downloadBlock(session.getUserId(), filename, blockIndex);

        return block;
    }

    @Override
    public List<String> listFiles(String token) throws RemoteException {

        List<String> filenames = null;

        if (authService.validateToken(token) == false) {
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
