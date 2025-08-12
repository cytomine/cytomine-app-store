package be.cytomine.appstore.exceptions;

import be.cytomine.appstore.dto.responses.errors.ErrorCode;
import be.cytomine.appstore.dto.responses.errors.ErrorDefinitions;

public class FileStorageException extends Exception {

    private ErrorCode errorCode;

    public FileStorageException(Exception e) {
        super(e);
    }

    public FileStorageException(ErrorCode e) {
        super(ErrorDefinitions.fromCode(e).getMessage());
        errorCode = e;
    }

    public FileStorageException(String message) {
        super(message);
    }
}
