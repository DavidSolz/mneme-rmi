package app.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Remote interface for a file service using Java RMI (Remote Method
 * Invocation).
 * <p>
 * This interface allows clients to upload and download file blocks, manage file
 * metadata,
 * retrieve file checksums, and list available files.
 * </p>
 *
 * All methods require a valid authentication token to ensure access control.
 */
public interface FileService extends Remote {

    /**
     * Sets the total number of blocks for a given file.
     *
     * @param token     Authentication token of the user.
     * @param filename  Name of the file.
     * @param numBlocks Total number of blocks that the file will contain.
     * @throws RemoteException If a remote communication error occurs.
     */
    void setFileBlockCount(String token, String filename, long numBlocks) throws RemoteException;

    /**
     * Retrieves the number of blocks that a given file contains.
     *
     * @param token    Authentication token of the user.
     * @param filename Name of the file.
     * @return Number of blocks in the file.
     * @throws RemoteException If a remote communication error occurs.
     */
    long getFileBlockCount(String token, String filename) throws RemoteException;

    /**
     * Uploads a single block of a file.
     *
     * @param token    Authentication token of the user.
     * @param filename Name of the file.
     * @param block    The block to upload, containing index and data.
     * @throws RemoteException If a remote communication error occurs.
     */
    void uploadBlock(String token, String filename, Block block) throws RemoteException;

    /**
     * Deletes a specified file from the system.
     *
     * @param token    Authentication token of the user.
     * @param filename Name of the file to delete.
     * @throws RemoteException If a remote communication error occurs.
     */
    void deleteFile(String token, String filename) throws RemoteException;

    /**
     * Retrieves a list of checksums (e.g., hashes) for the blocks of the specified
     * file.
     *
     * @param token    Authentication token of the user.
     * @param filename Name of the file.
     * @return List of checksums for each block of the file.
     * @throws RemoteException If a remote communication error occurs.
     */
    List<String> getChecksums(String token, String filename) throws RemoteException;

    /**
     * Downloads a specific block of a file.
     *
     * @param token      Authentication token of the user.
     * @param filename   Name of the file.
     * @param blockIndex Index of the block to download (0-based).
     * @return The requested file block.
     * @throws RemoteException If a remote communication error occurs.
     */
    Block downloadBlock(String token, String filename, long blockIndex) throws RemoteException;

    /**
     * Lists all files accessible to the user associated with the provided token.
     *
     * @param token Authentication token of the user.
     * @return List of filenames.
     * @throws RemoteException If a remote communication error occurs.
     */
    List<String> listFiles(String token) throws RemoteException;

    /**
     * Returns the fixed block size used for storing file data.
     *
     * @return The block size in bytes.
     * @throws RemoteException If a remote communication error occurs.
     */
    long getBlockSize() throws RemoteException;
}
