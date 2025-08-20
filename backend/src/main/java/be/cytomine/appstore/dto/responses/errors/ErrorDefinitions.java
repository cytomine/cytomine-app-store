package be.cytomine.appstore.dto.responses.errors;

import java.util.HashMap;

import be.cytomine.appstore.exceptions.UndefinedCodeException;

@SuppressWarnings("checkstyle:LineLength")
public class ErrorDefinitions {
    private static final HashMap<ErrorCode, MessageCode> codes;

    static {
        codes = new HashMap<>();
        codes.put(ErrorCode.INTERNAL_DESCRIPTOR_EXTRACTION_FAILED, new MessageCode("APPSTR-internal-descriptor-extraction-error", "failed to extract descriptor.yml from from bundle"));
        codes.put(ErrorCode.INTERNAL_DESCRIPTOR_NOT_IN_DEFAULT_LOCATION, new MessageCode("APPSTR-internal-bundle-validation-error", "descriptor is not found in the default location"));
        codes.put(ErrorCode.INTERNAL_DOCKER_IMAGE_EXTRACTION_FAILED, new MessageCode("APPSTR-internal-image-extraction-error", "failed to extract docker image tar from bundle"));
        codes.put(ErrorCode.INTERNAL_DOCKER_IMAGE_MANIFEST_MISSING, new MessageCode("APPSTR-internal-bundle-image-validation-error", "image is not invalid manifest is missing"));
        codes.put(ErrorCode.INTERNAL_DOCKER_IMAGE_TAR_NOT_FOUND, new MessageCode("APPSTR-internal-bundle-validation-error", "image not found in configured place in descriptor and not in the root directory"));
        codes.put(ErrorCode.INTERNAL_SCHEMA_VALIDATION_ERROR, new MessageCode("APPSTR-internal-bundle-schema-validation-error", "schema validation failed for descriptor.yml"));
        codes.put(ErrorCode.INTERNAL_SERVER_ERROR, new MessageCode("APPSTR-internal-server-error", "Server error."));
        codes.put(ErrorCode.INTERNAL_UNKNOWN_BUNDLE_ARCHIVE_FORAMT, new MessageCode("APPSTR-internal-bundle-validation-error", "unknown task bundle archive format"));
        codes.put(ErrorCode.INTERNAL_TASK_EXISTS, new MessageCode("APPSTR-internal-task-exists", "Task already exists."));
        codes.put(ErrorCode.INTERNAL_TASK_NOT_FOUND, new MessageCode("APPSTR-internal-task-not-found", "Task not found."));
        codes.put(ErrorCode.STORAGE_CREATING_STORAGE_FAILED, new MessageCode("APPSTR-storage-storage-creation-error", "creating storage failed in storage service"));
        codes.put(ErrorCode.INTERNAL_MISSING_OUTPUT_FILE_FOR_PARAMETER, new MessageCode("APPSTR-internal-missing-output-files", "file structure for primitive parameter is missing"));
        codes.put(ErrorCode.INTERNAL_OUTPUT_FILE_FOR_PARAMETER_IS_DIRECTORY, new MessageCode("APPSTR-internal-is-directory", "file structure for primitive parameter is a directory"));
        codes.put(ErrorCode.INTERNAL_EXTRA_OUTPUT_FILES_FOR_PARAMETER, new MessageCode("APPSTR-internal-extra-output-files", "file structure for parameter contains extra files"));
        codes.put(ErrorCode.INTERNAL_OUTPUT_FILE_FOR_PARAMETER_IS_BLANK, new MessageCode("APPSTR-internal-is-blank", "file for parameter is blank"));
        codes.put(ErrorCode.INTERNAL_MISSING_METADATA, new MessageCode("APPSTR-internal-missing-metadata", "collection does not have array.yml file"));
        codes.put(ErrorCode.INTERNAL_INVALID_FEATURE_COLLECTION, new MessageCode("APPSTR-internal-invalid-collection", "invalid feature collection"));
        codes.put(ErrorCode.INTERNAL_INVALID_COLLECTION_DIMENSIONS, new MessageCode("APPSTR-internal-invalid-dimensions", "invalid collection dimensions"));
        codes.put(ErrorCode.INTERNAL_UNKNOWN_PARAMETER, new MessageCode("APPSTR-internal-unknown", "unknown parameter"));
        codes.put(ErrorCode.INTERNAL_UNKNOWN_SUBTYPE, new MessageCode("APPSTR-internal-unknown-subtype", "collection subtype unknown"));
        codes.put(ErrorCode.INTERNAL_INVALID_METADATA, new MessageCode("APPSTR-internal-invalid-metadata", "collection array.yml is malformed"));
        codes.put(ErrorCode.STORAGE_STORING_TASK_DEFINITION_FAILED, new MessageCode("APPSTR-storage-definition-storage-error", "storing task definition failed in storage service"));
        codes.put(ErrorCode.REGISTRY_PUSHING_TASK_IMAGE_FAILED, new MessageCode("APPSTR-registry-push-failed", "pushing task image to registry failed in registry"));
        codes.put(ErrorCode.INTERNAL_PARAMETER_SCHEMA_VALIDATION_ERROR, new MessageCode("APPSTR-internal-bundle-io-schema-validation-error", ""));
        codes.put(ErrorCode.INTERNAL_UNKNOWN_IMAGE_ARCHIVE_FORMAT, new MessageCode("APPSTR-internal-image-validation-error", "unknown image archive format"));
        codes.put(ErrorCode.INTERNAL_MAX_UPLOAD_SIZE_EXCEEDED, new MessageCode("APPSTR-internal-bundle-validation-error", "maximum upload size for bundle exceeded"));
        codes.put(ErrorCode.INTERNAL_INVALID_BUNDLE_FORMAT, new MessageCode("APPSTR-internal-bundle-validation-error", "invalid bundle format"));
        codes.put(ErrorCode.INTERNAL_LOGO_EXTRACTION_FAILED, new MessageCode("APPSTR-internal-log-error", "failed to extract logo from bundle"));

    }

    public static MessageCode fromCode(ErrorCode code) {
        if (!codes.containsKey(code)) {
            throw new UndefinedCodeException(code);
        }
        return codes.get(code);
    }
}
