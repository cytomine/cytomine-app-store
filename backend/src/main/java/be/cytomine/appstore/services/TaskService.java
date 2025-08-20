package be.cytomine.appstore.services;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import be.cytomine.appstore.dto.handlers.filestorage.Storage;
import be.cytomine.appstore.dto.handlers.registry.DockerImage;
import be.cytomine.appstore.dto.inputs.task.TaskAuthor;
import be.cytomine.appstore.dto.inputs.task.TaskDescription;
import be.cytomine.appstore.dto.inputs.task.UploadTaskArchive;
import be.cytomine.appstore.dto.misc.TaskIdentifiers;
import be.cytomine.appstore.dto.responses.errors.AppStoreError;
import be.cytomine.appstore.dto.responses.errors.ErrorBuilder;
import be.cytomine.appstore.dto.responses.errors.ErrorCode;
import be.cytomine.appstore.exceptions.BundleArchiveException;
import be.cytomine.appstore.exceptions.FileStorageException;
import be.cytomine.appstore.exceptions.RegistryException;
import be.cytomine.appstore.exceptions.TaskNotFoundException;
import be.cytomine.appstore.exceptions.TaskServiceException;
import be.cytomine.appstore.exceptions.ValidationException;
import be.cytomine.appstore.handlers.RegistryHandler;
import be.cytomine.appstore.handlers.StorageData;
import be.cytomine.appstore.handlers.StorageDataEntry;
import be.cytomine.appstore.handlers.StorageDataType;
import be.cytomine.appstore.handlers.StorageHandler;
import be.cytomine.appstore.models.CheckTime;
import be.cytomine.appstore.models.Match;
import be.cytomine.appstore.models.Search;
import be.cytomine.appstore.models.task.Author;
import be.cytomine.appstore.models.task.Parameter;
import be.cytomine.appstore.models.task.ParameterType;
import be.cytomine.appstore.models.task.Task;
import be.cytomine.appstore.models.task.TypeFactory;
import be.cytomine.appstore.repositories.SearchRepository;
import be.cytomine.appstore.repositories.TaskRepository;
import be.cytomine.appstore.utils.ArchiveUtils;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final StorageHandler fileStorageHandler;

    private final RegistryHandler registryHandler;

    private final TaskValidationService taskValidationService;

    private final ArchiveUtils archiveUtils;

    private final SearchRepository searchRepository;

    @Value("${storage.input.charset}")
    private String charset;

    @Transactional
    public Optional<TaskDescription> uploadTask(MultipartFile taskArchive)
        throws BundleArchiveException, TaskServiceException, ValidationException {

        log.info("UploadTask: building archive...");
        UploadTaskArchive uploadTaskArchive = archiveUtils.readArchive(taskArchive);
        log.info("UploadTask: Archive is built");
        validateTaskBundle(uploadTaskArchive);
        log.info("UploadTask: Archive validated");

        TaskIdentifiers taskIdentifiers = generateTaskIdentifiers(uploadTaskArchive);
        log.info("UploadTask: Task identifiers generated {}", taskIdentifiers);

        Storage storage = new Storage(taskIdentifiers.getStorageIdentifier());
        try {
            fileStorageHandler.createStorage(storage);
            log.info("UploadTask: Storage is created for task");
        } catch (FileStorageException e) {
            log.error("UploadTask: failed to create storage [{}]", e.getMessage());
            AppStoreError error = ErrorBuilder.build(ErrorCode.STORAGE_CREATING_STORAGE_FAILED);
            throw new TaskServiceException(error);
        }

        try {
            fileStorageHandler.saveStorageData(
                storage,
                new StorageData(uploadTaskArchive.getDescriptorFile(), "descriptor.yml")
            );
            log.info("UploadTask: descriptor.yml is stored in object storage");

            if (Objects.nonNull(uploadTaskArchive.getLogo())) {
                fileStorageHandler.saveStorageData(
                    storage,
                    new StorageData(uploadTaskArchive.getLogo(), "logo.png")
                );
                log.info("UploadTask: logo.png is stored in object storage");
            }
        } catch (FileStorageException e) {
            try {
                log.info("UploadTask: failed to store file {}", e.getMessage());
                log.info("UploadTask: attempting deleting storage...");
                fileStorageHandler.deleteStorage(storage);
                log.info("UploadTask: storage deleted");
            } catch (FileStorageException ex) {
                log.error("UploadTask: file storage service is failing [{}]", ex.getMessage());
                AppStoreError error = ErrorBuilder
                    .build(ErrorCode.STORAGE_STORING_TASK_DEFINITION_FAILED);
                throw new TaskServiceException(error);
            }
            return Optional.empty();
        }

        log.info("UploadTask: pushing task image...");
        DockerImage image =
            new DockerImage(
            uploadTaskArchive.getDockerImage(),
            taskIdentifiers.getImageRegistryCompliantName());
        try {
            registryHandler.pushImage(image);
        } catch (RegistryException e) {
            try {
                log.debug("UploadTask: failed to push image to registry");
                log.debug("UploadTask: attempting to delete storage...");
                fileStorageHandler.deleteStorage(storage);
                log.info("UploadTask: storage deleted");
            } catch (FileStorageException ex) {
                log.error("UploadTask: file storage service is failing [{}]", ex.getMessage());
                AppStoreError error = ErrorBuilder
                    .build(ErrorCode.REGISTRY_PUSHING_TASK_IMAGE_FAILED);
                throw new TaskServiceException(error);
            }
        } finally {
            uploadTaskArchive.getDockerImage().delete();
        }
        log.info("UploadTask: image pushed to registry");

        // save task info
        Task task = new Task();
        task.setIdentifier(taskIdentifiers.getLocalTaskIdentifier());
        task.setStorageReference(taskIdentifiers.getStorageIdentifier());
        task.setImageName(taskIdentifiers.getImageRegistryCompliantName());
        task.setName(uploadTaskArchive.getDescriptorFileAsJson().get("name").textValue());
        task.setNameShort(uploadTaskArchive
            .getDescriptorFileAsJson()
            .get("name_short")
            .textValue());
        task.setDescriptorFile(
            uploadTaskArchive.getDescriptorFileAsJson().get("namespace").textValue());
        task.setDescription(uploadTaskArchive.getDescriptorFileAsJson().path("description").asText());
        task.setNamespace(uploadTaskArchive.getDescriptorFileAsJson().get("namespace").textValue());
        task.setVersion(uploadTaskArchive.getDescriptorFileAsJson().get("version").textValue());
        task.setInputFolder(
            uploadTaskArchive
            .getDescriptorFileAsJson()
            .get("configuration")
            .get("input_folder")
            .textValue());
        task.setOutputFolder(
            uploadTaskArchive
            .getDescriptorFileAsJson()
            .get("configuration")
            .get("output_folder")
            .textValue());

        // resources
        JsonNode resources =
            uploadTaskArchive.getDescriptorFileAsJson().get("configuration").get("resources");

        if (Objects.nonNull(resources)) {
            task.setRam(resources.path("ram").asText());
            task.setCpus(resources.path("cpus").asInt());
            task.setGpus(resources.path("gpus").asInt(0));
        }

        task.setAuthors(getAuthors(uploadTaskArchive));
        task.setParameters(getParameters(uploadTaskArchive));
        task.setMatches(getMatches(uploadTaskArchive, task.getParameters()));

        log.info("UploadTask: saving task...");
        taskRepository.save(task);
        log.info("UploadTask: task saved");
        log.info("UploadTask: update search index");
        searchRepository.refreshSearchViewConcurrently();
        log.info("UploadTask: search index updated");

        return Optional.of(makeTaskDescription(task));
    }

    private List<Match> getMatches(UploadTaskArchive uploadTaskArchive, Set<Parameter> parameters) {
        log.info("UploadTask: looking for matches...");
        JsonNode descriptor = uploadTaskArchive.getDescriptorFileAsJson();
        JsonNode inputsNode = descriptor.get("inputs");
        JsonNode outputsNode = descriptor.get("outputs");
        List<Match> matches = new ArrayList<>();

        // Process dependencies for inputs
        processDependencies(inputsNode, parameters, matches);

        // Process dependencies for outputs
        processDependencies(outputsNode, parameters, matches);

        log.info("UploadTask: matches processed successfully");
        return matches;
    }

    private void processDependencies(
        JsonNode node,
        Set<Parameter> parameters,
        List<Match> matches) {
        if (node != null && node.isObject()) {
            Iterator<String> fieldNames = node.fieldNames();
            while (fieldNames.hasNext()) {
                String key = fieldNames.next();
                JsonNode value = node.get(key);
                parameters.stream()
                    .filter(parameter -> parameter.getName().equals(key))
                    .findFirst()
                    .ifPresent(parameter -> processParameterDependencies(
                    parameter,
                    value,
                    parameters,
                    matches));
            }
        }
    }


    private void processParameterDependencies(
        Parameter param,
        JsonNode value,
        Set<Parameter> parameters,
        List<Match> matches) {
        JsonNode dependencies = value.get("dependencies");
        if (dependencies != null && dependencies.isObject()) {
            JsonNode matching = dependencies.get("matching");
            if (matching != null && matching.isArray()) {
                for (JsonNode element : matching) {
                    String text = element.textValue();
                    int slashIndex = text.indexOf("/");
                    if (slashIndex == -1) {
                        continue; // skip an unexpected format
                    }

                    String matchingType = text.substring(0, slashIndex);
                    String matchingName = text.substring(slashIndex + 1);

                    parameters.stream()
                        .filter(p -> p.getName()
                        .equals(matchingName) && p.getParameterType()
                        .equals(ParameterType.from(matchingType)))
                        .findFirst()
                        .ifPresent(other -> {
                            // set check time relative to execution
                            CheckTime when = CheckTime.UNDEFINED;
                            boolean bothInputs = param
                                .getParameterType()
                                .equals(ParameterType.INPUT)
                                && matchingType.equalsIgnoreCase("inputs");
                            if (bothInputs) {
                                when = CheckTime.BEFORE_EXECUTION;
                            }
                            boolean bothOutputs = param
                                .getParameterType()
                                .equals(ParameterType.OUTPUT)
                                && matchingType.equalsIgnoreCase("outputs");
                            boolean crossMatch = (param
                                .getParameterType()
                                .equals(ParameterType.INPUT)
                                && matchingType.equalsIgnoreCase("outputs"))
                                || param.getParameterType().equals(ParameterType.OUTPUT)
                                && matchingType.equalsIgnoreCase("inputs");
                            if (bothOutputs || crossMatch) {
                                when = CheckTime.AFTER_EXECUTION;
                            }

                            matches.add(new Match(param, other, when));
                        });

                }
            }
        }
    }

    private Set<Parameter> getParameters(UploadTaskArchive uploadTaskArchive) {
        log.info("UploadTask: getting inputs...");
        Set<Parameter> parameters = new HashSet<>();
        JsonNode inputsNode = uploadTaskArchive.getDescriptorFileAsJson().get("inputs");

        log.info("UploadTask: getting outputs...");
        JsonNode outputsNode = uploadTaskArchive.getDescriptorFileAsJson().get("outputs");

        if (!inputsNode.isObject() && !outputsNode.isObject()) {
            return new HashSet<>();
        }

        if (inputsNode.isObject()) {
            Iterator<String> fieldNames = inputsNode.fieldNames();
            while (fieldNames.hasNext()) {
                String inputKey = fieldNames.next();
                JsonNode inputValue = inputsNode.get(inputKey);

                Parameter input = new Parameter();
                input.setName(inputKey);
                input.setDisplayName(inputValue.get("display_name").textValue());
                input.setDescription(inputValue.get("description").textValue());
                // use type factory to generate the correct type
                input.setType(TypeFactory.createType(inputValue, charset));
                input.setParameterType(ParameterType.INPUT);
                // Set default value
                JsonNode defaultNode = inputValue.get("default");
                if (defaultNode != null) {
                    switch (defaultNode.getNodeType()) {
                        case STRING:
                            input.setDefaultValue(defaultNode.textValue());
                            break;
                        case BOOLEAN:
                            input.setDefaultValue(Boolean.toString(defaultNode.booleanValue()));
                            break;
                        case NUMBER:
                            input.setDefaultValue(defaultNode.numberValue().toString());
                            break;
                        default:
                            input.setDefaultValue(defaultNode.toString());
                            break;
                    }
                }

                parameters.add(input);
            }
        }

        log.info("UploadTask: successful input parameters");

        Iterator<String> outputFieldNames = outputsNode.fieldNames();
        while (outputFieldNames.hasNext()) {
            String outputKey = outputFieldNames.next();
            JsonNode outputValue = outputsNode.get(outputKey);

            Parameter output = new Parameter();
            output.setName(outputKey);
            output.setDisplayName(outputValue.get("display_name").textValue());
            output.setDescription(outputValue.get("description").textValue());
            output.setParameterType(ParameterType.OUTPUT);
            // use type factory to generate the correct type
            output.setType(TypeFactory.createType(outputValue, charset));

            JsonNode dependencies = outputValue.get("dependencies");
            if (dependencies != null && dependencies.isObject()) {
                JsonNode derivedFrom = dependencies.get("derived_from");
                String inputName = derivedFrom.textValue().substring("inputs/".length());
                parameters.stream()
                    .filter(parameter -> parameter.getName().equals(inputName)
                        && parameter.getParameterType().equals(ParameterType.INPUT))
                    .findFirst().ifPresent(output::setDerivedFrom);
            }

            parameters.add(output);
        }

        log.info("UploadTask: successful output parameters ");
        return parameters;
    }

    private Set<Author> getAuthors(UploadTaskArchive uploadTaskArchive) {
        log.info("UploadTask: getting authors...");
        Set<Author> authors = new HashSet<>();
        JsonNode authorNode = uploadTaskArchive.getDescriptorFileAsJson().get("authors");
        if (authorNode.isArray()) {
            for (JsonNode author : authorNode) {
                Author a = new Author();
                a.setFirstName(author.get("first_name").textValue());
                a.setLastName(author.get("last_name").textValue());
                a.setOrganization(author.get("organization").textValue());
                a.setEmail(author.get("email").textValue());
                a.setContact(author.get("is_contact").asBoolean());
                authors.add(a);
            }
        }
        log.info("UploadTask: successful authors ");
        return authors;
    }

    private void validateTaskBundle(UploadTaskArchive uploadTaskArchive)
        throws ValidationException {
        taskValidationService.validateDescriptorFile(uploadTaskArchive);
        taskValidationService.checkIsNotDuplicate(uploadTaskArchive);
        taskValidationService.validateImage(uploadTaskArchive);
    }

    private TaskIdentifiers generateTaskIdentifiers(UploadTaskArchive uploadTaskArchive) {
        UUID taskLocalIdentifier = UUID.randomUUID();
        String storageIdentifier = "task-" + taskLocalIdentifier + "-def";
        String imageIdentifierFromDescriptor =
            uploadTaskArchive.getDescriptorFileAsJson().get("namespace").textValue();
        String version = uploadTaskArchive.getDescriptorFileAsJson().get("version").textValue();
        String imageRegistryCompliantName = imageIdentifierFromDescriptor.replace(".", "/");
        imageRegistryCompliantName += ":" + version;

        return new TaskIdentifiers(taskLocalIdentifier,
            storageIdentifier,
            imageRegistryCompliantName);
    }

    public TaskDescription makeTaskDescription(Task task) {
        TaskDescription taskDescription =
            new TaskDescription(
            task.getIdentifier(),
            task.getName(),
            task.getNamespace(),
            task.getVersion(),
            task.getDescription());
        Set<TaskAuthor> descriptionAuthors = new HashSet<>();
        for (Author author : task.getAuthors()) {
            TaskAuthor taskAuthor =
                new TaskAuthor(
                author.getFirstName(),
                author.getLastName(),
                author.getOrganization(),
                author.getEmail(),
                author.isContact());
            descriptionAuthors.add(taskAuthor);
        }
        taskDescription.setAuthors(descriptionAuthors);
        return taskDescription;
    }

    public StorageData retrieveYmlDescriptor(String namespace, String version)
        throws TaskServiceException, TaskNotFoundException {
        log.info("Storage : retrieving descriptor.yml...");
        Task task = taskRepository.findByNamespaceAndVersion(namespace, version);
        if (task == null) {
            throw new TaskNotFoundException("task not found");
        }

        StorageData file = new StorageData("descriptor.yml", task.getStorageReference());
        try {
            file = fileStorageHandler.readStorageData(file);
        } catch (FileStorageException ex) {
            log.debug("Storage: failed to get file from storage [{}]", ex.getMessage());
            throw new TaskServiceException(ex);
        }
        return file;
    }

    public StorageData retrieveYmlDescriptor(String id)
        throws TaskServiceException, TaskNotFoundException {
        log.info("Storage : retrieving descriptor.yml using ID...");
        Optional<Task> task = taskRepository.findById(UUID.fromString(id));
        if (task.isEmpty()) {
            throw new TaskNotFoundException("task not found");
        }
        StorageData file = new StorageData("descriptor.yml", task.get().getStorageReference());
        try {
            file = fileStorageHandler.readStorageData(file);
        } catch (FileStorageException ex) {
            log.debug("Storage: failed to get descriptor file from storage [{}]", ex.getMessage());
            throw new TaskServiceException(ex);
        }
        return file;
    }

    public StorageData retrieveLogo(String namespace, String version)
        throws TaskServiceException, TaskNotFoundException {
        log.info("Storage : retrieving logo...");
        Task task = taskRepository.findByNamespaceAndVersion(namespace, version);
        if (task == null) {
            throw new TaskNotFoundException("task " + namespace + ":" + version + " not found");
        }

        StorageData file = new StorageData("logo.png", task.getStorageReference());
        try {
            file = fileStorageHandler.readStorageData(file);
        } catch (FileStorageException ex) {
            log.debug("Storage: failed to get logo from storage [{}]", ex.getMessage());
            AppStoreError error = ErrorBuilder.build(ErrorCode.INTERNAL_LOGO_NOT_FOUND);
            throw new TaskServiceException(error);
        }
        return file;
    }




    public List<TaskDescription> getAllTaskDescriptions() {
        return taskRepository.findAll()
                .stream()
                .map(this::makeTaskDescription)
                .toList();
    }

    public Optional<TaskDescription> retrieveTaskDescription(String id) {
        Optional<Task> task = findById(id);
        return task.map(this::makeTaskDescription);
    }

    public Optional<TaskDescription> retrieveTaskDescription(String namespace, String version) {
        Optional<Task> task = findByNamespaceAndVersion(namespace, version);
        return task.map(this::makeTaskDescription);
    }

    public Optional<Task> findByNamespaceAndVersion(String namespace, String version) {
        log.info("schemas/tasks/{namespace}/{version}: retrieving task...");
        Task task = taskRepository.findByNamespaceAndVersion(namespace, version);
        log.info("schemas/tasks/{namespace}/{version}: retrieved task...");
        return Optional.ofNullable(task);
    }

    public Optional<Task> findById(String id) {
        log.info("Data: retrieving task...");
        Optional<Task> task = taskRepository.findById(UUID.fromString(id));
        log.info("Data: retrieved task");
        return task;
    }


    public StorageData retrieveIOZipArchive(
        String namespace,
        String version
    ) throws FileStorageException, IOException, RegistryException, TaskNotFoundException {
        log.info("Retrieving IO Archive: retrieving...");
        Task task = taskRepository.findByNamespaceAndVersion(namespace, version);
        if (task == null) {
            throw new TaskNotFoundException("task not found");
        }
        log.info("Retrieving IO Archive: fetching descriptor.yml from storage...");
        StorageData descriptor = new StorageData("descriptor.yml", "task-" + task.getIdentifier() + "-def");
        log.info("Retrieving IO Archive: zipping...");
        Path tempFile = Files.createTempFile("bundle-", task.getIdentifier() + ".zip");
        ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(tempFile));
        StorageData destinationStorageData = fileStorageHandler.readStorageData(descriptor);
        for (StorageDataEntry current : destinationStorageData.getEntryList()) {
            ZipEntry zipEntry = new ZipEntry(current.getName());
            zipOut.putNextEntry(zipEntry);

            if (current.getStorageDataType().equals(StorageDataType.FILE)) {
                Files.copy(current.getData().toPath(), zipOut);
            }

            zipOut.closeEntry();
        }
        // get the registry
        log.info("Retrieving IO Archive: pulling image from registry...");
        ZipEntry zipEntry = new ZipEntry("image.tar");
        zipOut.putNextEntry(zipEntry);

        // Wrap zipOut so that close() only flushes rather than closing the underlying stream.
        OutputStream nonClosingStream = new FilterOutputStream(zipOut) {
            @Override
            public void close() throws IOException {
                flush();
            }
        };

        registryHandler.pullImage(task.getImageName(), nonClosingStream);
        log.info("Retrieving IO Archive: pulled");
        zipOut.closeEntry();
        zipOut.close();

        log.info("Retrieving IO Archive: zipped...");

        return new StorageData(tempFile.toFile());
    }

    public List<Search> search(String queryText) throws TaskServiceException {
        log.info("Search: searching for tasks...");
        if (queryText == null || queryText.isEmpty()) {
            log.error("Search: search text is empty or null");
            AppStoreError error = ErrorBuilder.build(ErrorCode.INTERNAL_EMPTY_SEARCH_QUERY);
            throw new TaskServiceException(error);
        }
        List<Search> hits = searchRepository.findByAdvancedSearch(queryText.trim().replace(" ", "&"), queryText);
        log.info("Search: search done");
        return hits;

    }
}
