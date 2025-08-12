package be.cytomine.appstore.exceptions;

import be.cytomine.appstore.dto.responses.errors.AppStoreError;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TaskServiceException extends Exception {

    private AppStoreError error;

    public TaskServiceException(AppStoreError error) {
        super();
        this.error = error;
    }

    public TaskServiceException(Exception e) {
        super(e);
    }

    public TaskServiceException(String message) {
        super(message);
    }
}
