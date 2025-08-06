package be.cytomine.appstore.controllers;

import java.util.Optional;

import be.cytomine.appstore.dto.inputs.task.TaskDescription;
import be.cytomine.appstore.exceptions.BundleArchiveException;
import be.cytomine.appstore.exceptions.TaskServiceException;
import be.cytomine.appstore.exceptions.ValidationException;
import be.cytomine.appstore.services.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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


}
