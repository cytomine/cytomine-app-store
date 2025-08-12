package be.cytomine.appstore.dto.responses.errors;

import java.util.List;

import be.cytomine.appstore.dto.responses.errors.details.BaseErrorDetails;
import be.cytomine.appstore.dto.responses.errors.details.BatchError;
import be.cytomine.appstore.dto.responses.errors.details.ServerError;

public class ErrorBuilder {

    /**
     * Build an error with empty details object
     *
     * @param code The error code
     */
    public static AppStoreError build(ErrorCode code) {
        return build(code, new BaseErrorDetails());
    }

    public static AppStoreError build(ErrorCode code, BaseErrorDetails details) {
        MessageCode container = ErrorDefinitions.fromCode(code);
        return new AppStoreError(container.code, container.message, details);
    }

    public static AppStoreError buildWithMessage(
        ErrorCode code,
        String message,
        BaseErrorDetails details
    ) {
        MessageCode container = ErrorDefinitions.fromCode(code);
        return new AppStoreError(container.code, message, details);
    }

    public static AppStoreError buildSchemaValidationError(List<AppStoreError> errors) {
        ErrorCode code = ErrorCode.INTERNAL_SCHEMA_VALIDATION_ERROR;
        return build(code, new BatchError(errors));
    }

    /**
     * Build a server error
     *
     * @param e Exception triggered by the server error
     */
    public static AppStoreError buildServerError(Exception e) {
        ServerError serverError = new ServerError(e.getMessage());
        return build(ErrorCode.INTERNAL_SERVER_ERROR, serverError);
    }
}
