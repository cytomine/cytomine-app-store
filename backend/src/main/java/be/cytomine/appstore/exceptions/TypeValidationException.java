package be.cytomine.appstore.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

import be.cytomine.appstore.dto.responses.errors.AppStoreError;
import be.cytomine.appstore.dto.responses.errors.ErrorCode;
import be.cytomine.appstore.dto.responses.errors.ErrorDefinitions;

@Data
@EqualsAndHashCode(callSuper = false)
public class TypeValidationException extends Exception {

    private AppStoreError error;

    private ErrorCode errorCode;

    public TypeValidationException(Exception e) {
        super(e);
    }

    public TypeValidationException(ErrorCode e) {
        super(ErrorDefinitions.fromCode(e).getMessage());
        errorCode = e;
    }

    public TypeValidationException(AppStoreError error) {
        super();
        this.error = error;
    }

    public TypeValidationException(String message) {
        super(message);
    }
}
