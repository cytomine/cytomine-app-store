package be.cytomine.appstore.exceptions;

import be.cytomine.appstore.dto.responses.errors.AppStoreError;
import be.cytomine.appstore.dto.responses.errors.ErrorCode;
import be.cytomine.appstore.dto.responses.errors.ErrorDefinitions;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProvisioningException extends Exception {

    private AppStoreError error;
    private ErrorCode errorCode;

    public ProvisioningException(Exception e) {
        super(e);
    }

    public ProvisioningException(AppStoreError error) {
        super(error.getMessage());
        this.error = error;
    }

    public ProvisioningException(ErrorCode e) {
        super(ErrorDefinitions.fromCode(e).getMessage());
        errorCode = e;
    }

    public ProvisioningException(String message) {
        super(message);
    }
}
