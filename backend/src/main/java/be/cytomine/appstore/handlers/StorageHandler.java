package be.cytomine.appstore.handlers;

import be.cytomine.appstore.dto.handlers.filestorage.Storage;
import be.cytomine.appstore.exceptions.FileStorageException;

public interface StorageHandler {
    void createStorage(Storage storage) throws FileStorageException;

    void deleteStorage(Storage storage) throws FileStorageException;

    boolean checkStorageExists(Storage storage) throws FileStorageException;

    boolean checkStorageExists(String idStorage) throws FileStorageException;

    void deleteStorageData(StorageData file) throws FileStorageException;

    void saveStorageData(Storage storage, StorageData storageData) throws FileStorageException;

    StorageData readStorageData(StorageData emptyFile) throws FileStorageException;
}
