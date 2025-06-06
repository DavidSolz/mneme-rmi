package app.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import app.common.Block;
import app.common.CrypticEngine;
import app.common.FileService;

public class FileClient {
    private FileService fileService;
    private int clientID;
    
    public FileClient(FileService fileService){
        this.fileService = fileService;
        this.clientID = -1;
    }
    
    
    public void upload(String filename, String token){
        List<String> checksums;
        List<Block> blocks;
        Block block;
        long blockSize;
        byte [] data;
        byte [] dataPart;
        Path path = Paths.get(filename);
        try {
            checksums = fileService.getChecksums(token, filename);
            blockSize = fileService.getBlockSize();
            blocks = new LinkedList<>();
            
            data = Files.readAllBytes(path);
            for(int i = 0; i < data.length; i += blockSize){
                int realBlockSize = (int)Math.min(blockSize, data.length - i + 1);
                dataPart = new byte[realBlockSize];
                block = new Block();
                
                System.arraycopy(data, i, dataPart, 0, realBlockSize);
                block.setUserId(this.clientID);
                block.setSequenceNumber(i / blockSize);
                block.setData(dataPart);
                block.setSize(realBlockSize);
                block.setChecksum(CrypticEngine.weakHash(dataPart, realBlockSize));
                
                blocks.add(block);
            }
            
            fileService.setFileBlockCount(token, filename, blocks.size());
            
            for(int i = 0; i < blocks.size(); ++i){
                if(blocks.get(i).getChecksum() != checksums.get(i)){
                    fileService.uploadBlock(token, filename, blocks.get(i));
                }
            }
            
            
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void delete(String filename, String token){
        List<String> checksums;
        try {
            checksums = fileService.getChecksums(token, filename);
            if(checksums.size() == 0){
                System.out.println("Brak pliku o takiej nazwie");
            }
            else{
                fileService.deleteFile(token, filename);
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }
    
    public void download(String filename, String localPath, String token){
        List<Block> blocks;
        long blockCount;
        int helperId;
        int fileTotalSize;
        byte [] data;
        Path path = Paths.get(localPath);
        
        try {
            blockCount = fileService.getFileBlockCount(token, filename);
            blocks = new LinkedList<>();
            for(int i = 0; i < blockCount; ++i){
                blocks.add(fileService.downloadBlock(token, filename, i));
            }
            helperId = 0;
            fileTotalSize = 0;
            for(Block i:blocks){
                fileTotalSize += i.getSize();
            }
            data = new byte[fileTotalSize];
            for(Block i:blocks){
                System.arraycopy(i.getData(), 0, data, helperId, i.getSize());
                helperId += i.getSize();
            }
            Files.write(path, data);
            
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    public List<String> listFiles(String token){
        try {
            return fileService.listFiles(token);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    public void setClientID(int clientID) {
        this.clientID = clientID;
    }
    
    public int getClientID() {
        return clientID;
    }

}
