package be.cytomine.appstore.controllers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import be.cytomine.appstore.dto.inputs.task.TaskDescription;
import be.cytomine.appstore.dto.responses.errors.ErrorBuilder;
import be.cytomine.appstore.dto.responses.errors.ErrorCode;
import be.cytomine.appstore.exceptions.BundleArchiveException;
import be.cytomine.appstore.exceptions.FileStorageException;
import be.cytomine.appstore.exceptions.RegistryException;
import be.cytomine.appstore.exceptions.TaskNotFoundException;
import be.cytomine.appstore.exceptions.TaskServiceException;
import be.cytomine.appstore.exceptions.ValidationException;
import be.cytomine.appstore.handlers.StorageData;
import be.cytomine.appstore.models.Search;
import be.cytomine.appstore.services.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "${app-store.api_prefix}${app-store.api_version}/")
public class TaskController
{

    private final TaskService taskService;

    @PostMapping(path = "tasks")
    public ResponseEntity<?> upload(
        @RequestParam MultipartFile task
    ) throws TaskServiceException, ValidationException, BundleArchiveException {
        log.info("Task Upload POST");
        Optional<TaskDescription> taskDescription = taskService.uploadTask(task);
        log.info("Task Upload POST Ended");
        return ResponseEntity.ok(taskDescription);
    }

    @GetMapping(value = "tasks/{namespace}/{version}/bundle.zip")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> getTaskBundle(
        @PathVariable("namespace") String namespace,
        @PathVariable("version") String version
    ) throws IOException, FileStorageException, RegistryException, TaskNotFoundException
    {
        log.info("tasks/{namespace}/{version}/bundle.zip GET");
        StorageData data = taskService.retrieveIOZipArchive(namespace, version);
        File file = data.peek().getData();

        HttpHeaders headers = new HttpHeaders();
        headers.add(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=bundle.zip"
        );
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        log.info("tasks/{namespace}/{version}/bundle.zip GET Ended");
        return ResponseEntity.ok()
            .headers(headers)
            .body(new FileSystemResource(file));
    }

    @GetMapping(value = "tasks/search")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> searchTasks(
        @RequestParam("query") String query
    ) throws TaskServiceException
    {
        log.info("tasks/search GET {}", query);
        List<Search> data = taskService.search(query);

        log.info("tasks/search GET Ended");
        return ResponseEntity.ok(data);
    }

    @GetMapping(value = "tasks/{namespace}/{version}/descriptor.yml")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> findDescriptorOfTaskByNamespaceAndVersion(
        @PathVariable String namespace,
        @PathVariable String version
    ) throws TaskServiceException, TaskNotFoundException {
        log.info("tasks/{namespace}/{version}/descriptor.yml GET");
        StorageData data = taskService.retrieveYmlDescriptor(namespace, version);
        File file = data.peek().getData();

        HttpHeaders headers = new HttpHeaders();
        headers.add(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + file.getName() + "\""
        );
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        log.info("tasks/{namespace}/{version}/descriptor.yml GET Ended");
        return ResponseEntity.ok()
            .headers(headers)
            .body(new FileSystemResource(file));
    }


}
