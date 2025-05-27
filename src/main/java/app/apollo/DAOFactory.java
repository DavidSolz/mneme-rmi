package app.apollo;

public interface DAOFactory {
    SessionDAO getSessionDAO();
    UserDAO getUserDAO();
    FileMetadataDAO getFileMetadataDAO();
    FileBlockDAO getFileBlockDAO();
}
