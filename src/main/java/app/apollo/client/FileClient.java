package app.apollo.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import app.apollo.common.Block;
import app.apollo.common.CrypticEngine;
import app.apollo.common.FileService;

/**
 * FileClient is responsible for interacting with the remote FileService.
 * It provides methods for uploading, downloading, deleting, and listing files.
 */
public class FileClient {
    private FileService fileService; // Remote file service for file operations.
    private int clientID; // ID of the client using this FileClient instance.
    private static String lastLoggedUserFileName; // File storing the last logged user's ID.

    /**
     * Constructor to initialize the FileClient with a remote FileService.
     * It also attempts to load the last logged user's ID from a file.
     * 
     * @param fileService The remote file service to use.
     */
    public FileClient(FileService fileService) {
        this.fileService = fileService;

        // Check if the file storing the last logged user's ID exists.
        if (Files.exists(Paths.get(lastLoggedUserFileName))) {
            try {
                // Read the client ID from the file.
                this.clientID = Integer.parseInt(new String(Files.readAllBytes(Paths.get(lastLoggedUserFileName))));
            } catch (NumberFormatException e) {
                // Handle invalid number format in the file.
                System.out.println(e.getMessage());
            } catch (IOException e) {
                // Handle file read errors.
                System.out.println(e.getMessage());
            }
        } else {
            // Set default client ID if the file does not exist.
            this.clientID = -1;
        }
    }

    /**
     * Sets the file name used to store the last logged user's ID.
     * 
     * @param lastLoggedUserFileName The file name to set.
     */
    public static void setLastLoggedUserFileName(String lastLoggedUserFileName) {
        FileClient.lastLoggedUserFileName = lastLoggedUserFileName;
    }

    /**
     * Prints a progress bar for file operations.
     * 
     * @param operation      The name of the operation (e.g., "Uploading").
     * @param percent        The percentage of completion.
     * @param remainingMillis The estimated remaining time in milliseconds.
     */
    private void printProgress(String operation, double percent, long remainingMillis) {
        int width = 30; // Width of the progress bar.
        int pos = (int) (percent / 100.0 * width);
        StringBuilder bar = new StringBuilder();
        bar.append("\r").append(operation).append(" [");

        for (int i = 0; i < width; ++i) {
            if (i < pos)
                bar.append("=");
            else if (i == pos)
                bar.append(">");
            else
                bar.append(" ");
        }

        bar.append("] ");
        bar.append(String.format("%.1f", percent)).append("%");

        long seconds = remainingMillis / 1000;
        bar.append(" - ETA: ").append(seconds).append("s");

        System.out.print(bar);
    }

    /**
     * Uploads a file to the remote service.
     * 
     * @param filename The name of the file to upload.
     * @param token    The authentication token of the user.
     */
    public void upload(String filename, String token) {
        System.out.println(token);
        List<String> checksums;
        List<Block> blocks;
        Block block;
        long blockSize;
        byte[] data;
        byte[] dataPart;
        Path path = Paths.get(filename);
        try {
            // Retrieve existing checksums and block size from the remote service.
            checksums = fileService.getChecksums(token, filename);
            blockSize = fileService.getBlockSize();

            blocks = new LinkedList<>();

            // Read the file data and divide it into blocks.
            data = Files.readAllBytes(path);
            for (int i = 0; i < data.length; i += blockSize) {
                int realBlockSize = (int) Math.min(blockSize, data.length - i);
                dataPart = new byte[realBlockSize];
                block = new Block();

                // Copy data into the block and set its properties.
                System.arraycopy(data, i, dataPart, 0, realBlockSize);
                block.setUserId(this.clientID);
                block.setSequenceNumber(i / blockSize);
                block.setData(dataPart);
                block.setSize(realBlockSize);
                block.setChecksum(CrypticEngine.weakHash(dataPart, realBlockSize));

                blocks.add(block);
            }

            // Set the total number of blocks in the remote service.
            fileService.setFileBlockCount(token, filename, blocks.size());

            long startTime = System.currentTimeMillis();
            int totalBlocks = blocks.size();
            int uploadedCount = 0;

            // Upload each block to the remote service.
            for (int i = 0; i < blocks.size(); ++i) {
                if (i < checksums.size()) {
                    if (blocks.get(i).getChecksum() != checksums.get(i)) {
                        fileService.uploadBlock(token, filename, blocks.get(i));
                    }
                } else {
                    fileService.uploadBlock(token, filename, blocks.get(i));
                }

                uploadedCount++;
                long elapsed = System.currentTimeMillis() - startTime;
                double progress = (uploadedCount * 100.0) / totalBlocks;
                double avgTimePerBlock = elapsed / (double) uploadedCount;
                long remainingTime = (long) ((totalBlocks - uploadedCount) * avgTimePerBlock);

                printProgress("Wysyłanie", progress, remainingTime);
            }

            System.out.println("\nWysłano plik");

        } catch (RemoteException e) {
            // Handle remote communication errors.
            System.out.println(e.getMessage());
        } catch (IOException e) {
            // Handle file read/write errors.
            System.out.println(e.getMessage());
        }
    }

    /**
     * Deletes a file from the remote service.
     * 
     * @param filename The name of the file to delete.
     * @param token    The authentication token of the user.
     */
    public void delete(String filename, String token) {
        List<String> checksums;
        try {
            // Retrieve checksums to verify file existence.
            checksums = fileService.getChecksums(token, filename);
            if (checksums.size() == 0) {
                System.out.println("Brak pliku o takiej nazwie");
            } else {
                fileService.deleteFile(token, filename);
            }
        } catch (RemoteException e) {
            // Handle remote communication errors.
            System.out.println(e.getMessage());
        }
    }

    /**
     * Downloads a file from the remote service.
     * 
     * @param filename  The name of the file to download.
     * @param localPath The local path to save the downloaded file.
     * @param token     The authentication token of the user.
     */
    public void download(String filename, String localPath, String token) {
        List<Block> blocks;
        Block temp;
        String tempHash;
        long blockCount;
        int helperId;
        int fileTotalSize;
        byte[] data;
        Path path = Paths.get(localPath);
        boolean fileExists;

        try {
            // Ensure the local directory exists.
            String[] directories = localPath.split("/");
            StringBuilder directory = new StringBuilder();
            for (int i = 0; i < directories.length - 1; ++i) {
                directory.append(directories[i]).append("/");
            }
            if (directories.length > 1) {
                Path localPath2 = Paths.get(directory.toString());
                if (!Files.exists(localPath2)) {
                    Files.createDirectories(localPath2);
                }
            }

            // Check if the file exists on the remote service.
            fileExists = false;
            List<String> fileList = fileService.listFiles(token);
            for (String i : fileList) {
                if (i.equals(filename)) {
                    fileExists = true;
                }
            }

            if (!fileExists) {
                System.out.println("Plik o takiej nazwie nie istnieje");
            } else {
                // Retrieve the number of blocks in the file.
                blockCount = fileService.getFileBlockCount(token, filename);
                System.out.println("Plik zawiera " + blockCount + " bloki");

                blocks = new LinkedList<>();
                long startTime = System.currentTimeMillis();

                // Download each block and verify its checksum.
                for (int i = 0; i < blockCount; ++i) {
                    temp = fileService.downloadBlock(token, filename, i);
                    tempHash = CrypticEngine.weakHash(temp.getData(), temp.getSize());
                    while (!tempHash.equals(temp.getChecksum())) {
                        System.out.println("Blok pobrany błędnie, popnowne pobieranie");
                        temp = fileService.downloadBlock(token, filename, i);
                        tempHash = CrypticEngine.weakHash(temp.getData(), temp.getSize());
                    }
                    blocks.add(temp);

                    long elapsed = System.currentTimeMillis() - startTime;
                    double progress = ((i + 1) * 100.0) / blockCount;
                    double avgTimePerBlock = elapsed / (double) (i + 1);
                    long remainingTime = (long) ((blockCount - (i + 1)) * avgTimePerBlock);

                    printProgress("Pobieranie", progress, remainingTime);
                }

                // Combine all blocks into a single file.
                helperId = 0;
                fileTotalSize = 0;
                for (Block i : blocks) {
                    fileTotalSize += i.getSize();
                }
                data = new byte[fileTotalSize];
                for (Block i : blocks) {
                    System.arraycopy(i.getData(), 0, data, helperId, i.getSize());
                    helperId += i.getSize();
                }
                Files.write(path, data);
                System.out.println("\nPobrano plik");
            }

        } catch (RemoteException e) {
            // Handle remote communication errors.
            System.out.println(e.getMessage());
        } catch (IOException e) {
            // Handle file read/write errors.
            System.out.println(e.getMessage());
        }
    }

    /**
     * Lists all files available on the remote service.
     * 
     * @param token The authentication token of the user.
     * @return A list of file names.
     */
    public List<String> listFiles(String token) {
        try {
            return fileService.listFiles(token);
        } catch (RemoteException e) {
            // Handle remote communication errors.
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Sets the client ID for this FileClient instance.
     * 
     * @param clientID The client ID to set.
     */
    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    /**
     * Retrieves the client ID for this FileClient instance.
     * 
     * @return The client ID.
     */
    public int getClientID() {
        return clientID;
    }
}
