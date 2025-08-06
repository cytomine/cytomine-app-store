package be.cytomine.appstore.dto.responses.errors;

import java.util.List;

import be.cytomine.appstore.dto.responses.errors.details.BaseErrorDetails;
import be.cytomine.appstore.dto.responses.errors.details.BatchError;
import be.cytomine.appstore.dto.responses.errors.details.EmptyErrorDetails;
import be.cytomine.appstore.dto.responses.errors.details.MultipleErrors;
import be.cytomine.appstore.dto.responses.errors.details.ParamRelatedError;
import be.cytomine.appstore.dto.responses.errors.details.ServerError;

public class ErrorBuilder {

    /**
     * Build an error with empty details object
     *
     * @param code The error code
     */
    public static AppStoreError build(ErrorCode code) {
        return build(code, new EmptyErrorDetails());
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

    /***
     * Build an error message for errors originating from a batch request
     *
     * @param errors list of underlying errors, each error corresponds to an item of
     *               the batch
     */
    public static AppStoreError buildBatchError(List<AppStoreError> errors) {
        ErrorCode code = ErrorCode.INTERNAL_GENERIC_BATCH_ERROR;
        return build(code, new BatchError(errors));
    }

    public static AppStoreError buildSchemaValidationError(List<AppStoreError> errors) {
        ErrorCode code = ErrorCode.INTERNAL_SCHEMA_VALIDATION_ERROR;
        return build(code, new BatchError(errors));
    }

    public static AppStoreError buildMultipleErrors(List<AppStoreError> errors) {
        ErrorCode code = ErrorCode.INTERNAL_MULTIPLE_ERRORS;
        return build(code, new MultipleErrors(errors));
    }

    /**
     * Build an error originating from a batch param
     *
     * @param code        top-level error code
     * @param paramName   param name
     * @param description param error description
     */
    public static AppStoreError buildParamRelatedError(
        ErrorCode code,
        String paramName,
        String description
    ) {
        return build(code, new ParamRelatedError(paramName, description));
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
