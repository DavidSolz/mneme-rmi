package app.client;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import app.common.AuthService;
import app.common.Block;
import app.common.CrypticEngine;
import app.common.FileService;
import app.common.Session;

public class App {

    public static void main(String[] args) {

        AuthService authService = null;
        FileService fileService = null;

        try {
            String portString = System.getenv("PORT");
            Integer port = portString != null ? Integer.parseInt(portString) : 2567;
            Registry registry = LocateRegistry.getRegistry(port);

            authService = (AuthService) registry.lookup("AuthService");
            fileService = (FileService) registry.lookup("FileService");

            // boolean result = authService.register("admin", "admin");

            // if( result == false )
            // {
            // System.out.println("Failed to create accout with this credentials.");
            // }

            Session session = authService.login("admin", "admin");

            System.out.println("Token: " + session.getToken());

            String filename = "test.txt";

            byte[] data = Files.readAllBytes(Paths.get(filename));

            long blockSize = fileService.getBlockSize();
            int totalBlocks = (int) Math.ceil((double) data.length / blockSize);

            List<String> checksums = fileService.getChecksums(session.getToken(), filename);

            System.out.println("Uploading in " + totalBlocks + " blocks");

            for (int i = 0; i < totalBlocks; i++) {
                int start = (int) (i * blockSize);
                int end = Math.min(start + (int) blockSize, data.length);
                byte[] blockData = new byte[end - start];
                System.arraycopy(data, start, blockData, 0, end - start);

                String checksum = CrypticEngine.weakHash(blockData, blockData.length);
                String old = checksums.get(i);

                if(old.equals(checksum) == false)
                {

                    Block block = new Block();
                    block.setUserId(session.getUserId());
                    block.setData(blockData);
                    block.setSequenceNumber((long) i);
                    block.setChecksum(checksum);
                    block.setSize(blockData.length);

                    fileService.uploadBlock(session.getToken(), filename, block);
                    System.out.println("Wysłano " + i + " blok.");
                }

            }

            System.out.println("Upload complete.");

            Block b = fileService.downloadBlock(session.getToken(), filename, 0);
            if (b != null) {
                System.out.println("Downloaded Block 0: " + new String(b.getData()));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
