package be.cytomine.appstore.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

import be.cytomine.appstore.dto.responses.errors.AppStoreError;

@Data
@EqualsAndHashCode(callSuper = false)
public class BundleArchiveException extends Exception {

    AppStoreError error;

    public BundleArchiveException(AppStoreError error) {

        super();
        this.error = error;
    }

    public BundleArchiveException(Exception e) {
        super(e);
    }

    public BundleArchiveException(String message) {
        super(message);
    }
}
