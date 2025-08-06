package be.cytomine.appstore.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import be.cytomine.appstore.dto.handlers.filestorage.Storage;
import be.cytomine.appstore.dto.handlers.registry.DockerImage;
import be.cytomine.appstore.dto.inputs.task.TaskDescription;
import be.cytomine.appstore.dto.inputs.task.UploadTaskArchive;
import be.cytomine.appstore.exceptions.FileStorageException;
import be.cytomine.appstore.exceptions.RunTaskServiceException;
import be.cytomine.appstore.exceptions.TaskNotFoundException;
import be.cytomine.appstore.exceptions.TaskServiceException;
import be.cytomine.appstore.handlers.RegistryHandler;
import be.cytomine.appstore.handlers.StorageData;
import be.cytomine.appstore.handlers.StorageHandler;
import be.cytomine.appstore.models.task.Task;
import be.cytomine.appstore.repositories.TaskRepository;
import be.cytomine.appstore.utils.ArchiveUtils;
import be.cytomine.appstore.utils.TaskUtils;
import be.cytomine.appstore.utils.TestTaskBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private ArchiveUtils archiveUtils;

    @Mock
    private RegistryHandler registryHandler;

    @Mock
    private StorageHandler storageHandler;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskValidationService taskValidationService;

    @InjectMocks
    private TaskService taskService;

    private static Task task;

    private static UploadTaskArchive uploadTaskArchive;

    @BeforeAll
    public static void setUp() throws Exception {
        task = TaskUtils.createTestTask(false);
        uploadTaskArchive = TaskUtils.createTestUploadTaskArchive();
    }

    @DisplayName("Successfully upload a task bundle")
    @Test
    public void uploadTaskShouldUploadTaskBundle() throws Exception {
        ClassPathResource resource = TestTaskBuilder.buildCustomImageLocationTask();
        MockMultipartFile testAppBundle = new MockMultipartFile("test_custom_image_location_task.zip", resource.getInputStream());

        when(archiveUtils.readArchive(testAppBundle)).thenReturn(uploadTaskArchive);
        Optional<TaskDescription> result = taskService.uploadTask(testAppBundle);

        assertTrue(result.isPresent());
        verify(archiveUtils, times(1)).readArchive(testAppBundle);
        verify(storageHandler, times(1)).createStorage(any(Storage.class));
        verify(storageHandler, times(1)).saveStorageData(any(Storage.class), any(StorageData.class));
        verify(registryHandler, times(1)).pushImage(any(DockerImage.class));
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @DisplayName("Successfully retrieve the descriptor by namespace and version")
    @Test
    public void retrieveYmlDescriptorByNamespaceAndVersionShouldReturnDescriptor() throws Exception {
        String namespace = "namespace";
        String version = "version";
        StorageData mockStorageData = new StorageData("descriptor.yml", "storageReference");
        when(taskRepository.findByNamespaceAndVersion(namespace, version)).thenReturn(task);
        when(storageHandler.readStorageData(any(StorageData.class))).thenReturn(mockStorageData);

        StorageData result = taskService.retrieveYmlDescriptor(namespace, version);

        assertNotNull(result);
        assertEquals("descriptor.yml", result.peek().getName());
        assertEquals("storageReference", result.peek().getStorageId());
        verify(taskRepository, times(1)).findByNamespaceAndVersion(namespace, version);
        verify(storageHandler, times(1)).readStorageData(any(StorageData.class));
    }

    @DisplayName("Fail to retrieve the descriptor by namespace and version and throw TaskNotFoundException")
    @Test
    public void retrieveYmlDescriptorByNamespaceAndVersionShouldThrowTaskNotFoundException() throws Exception {
        String namespace = "namespace";
        String version = "version";
        when(taskRepository.findByNamespaceAndVersion(namespace, version)).thenReturn(null);

        TaskNotFoundException exception = assertThrows(
            TaskNotFoundException.class,
            () -> taskService.retrieveYmlDescriptor(namespace, version)
        );
        assertEquals("task not found", exception.getMessage());
        verify(taskRepository, times(1)).findByNamespaceAndVersion(namespace, version);
        verify(storageHandler, times(0)).readStorageData(any(StorageData.class));
    }

    @DisplayName("Fail to retrieve the descriptor by namespace and version and throw FileStorageException")
    @Test
    public void retrieveYmlDescriptorByNamespaceAndVersionShouldThrowFileStorageException() throws Exception {
        String namespace = "namespace";
        String version = "version";
        when(taskRepository.findByNamespaceAndVersion(namespace, version)).thenReturn(task);
        when(storageHandler.readStorageData(any(StorageData.class)))
            .thenThrow(new FileStorageException("File error"));

        TaskServiceException exception = assertThrows(
            TaskServiceException.class,
            () -> taskService.retrieveYmlDescriptor(namespace, version)
        );
        assertInstanceOf(FileStorageException.class, exception.getCause());
        verify(taskRepository, times(1)).findByNamespaceAndVersion(namespace, version);
        verify(storageHandler, times(1)).readStorageData(any(StorageData.class));
    }

    @DisplayName("Successfully retrieve the descriptor by ID")
    @Test
    public void retrieveYmlDescriptorByIdShouldReturnDescriptor() throws Exception {
        String id = "d9aad8ab-210c-48fa-8d94-6b03e8776a55";
        StorageData mockStorageData = new StorageData("descriptor.yml", "storageReference");
        when(taskRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(task));
        when(storageHandler.readStorageData(any(StorageData.class))).thenReturn(mockStorageData);

        StorageData result = taskService.retrieveYmlDescriptor(id);

        assertNotNull(result);
        assertEquals("descriptor.yml", result.peek().getName());
        assertEquals("storageReference", result.peek().getStorageId());
        verify(taskRepository, times(1)).findById(UUID.fromString(id));
        verify(storageHandler, times(1)).readStorageData(any(StorageData.class));
    }

    @DisplayName("Fail to retrieve the descriptor by ID and throw TaskNotFoundException")
    @Test
    public void retrieveYmlDescriptorByIdShouldThrowTaskNotFoundException() throws Exception {
        when(taskRepository.findById(task.getIdentifier())).thenReturn(Optional.empty());

        TaskNotFoundException exception = assertThrows(
            TaskNotFoundException.class,
            () -> taskService.retrieveYmlDescriptor(task.getIdentifier().toString())
        );
        assertEquals("task not found", exception.getMessage());
        verify(taskRepository, times(1)).findById(task.getIdentifier());
        verify(storageHandler, times(0)).readStorageData(any(StorageData.class));
    }

    @DisplayName("Fail to retrieve the descriptor by ID and throw FileStorageException")
    @Test
    public void retrieveYmlDescriptorByIdShouldThrowFileStorageException() throws Exception {
        when(taskRepository.findById(task.getIdentifier())).thenReturn(Optional.of(task));
        when(storageHandler.readStorageData(any(StorageData.class)))
            .thenThrow(new FileStorageException("File error"));

        TaskServiceException exception = assertThrows(
            TaskServiceException.class,
            () -> taskService.retrieveYmlDescriptor(task.getIdentifier().toString())
        );
        assertInstanceOf(FileStorageException.class, exception.getCause());
        verify(taskRepository, times(1)).findById(task.getIdentifier());
        verify(storageHandler, times(1)).readStorageData(any(StorageData.class));
    }

    @DisplayName("Successfully retrieve the task description by ID")
    @Test
    void retrieveTaskDescriptionByIdShouldReturnTaskDescription() {
        when(taskRepository.findById(task.getIdentifier())).thenReturn(Optional.of(task));

        Optional<TaskDescription> result = taskService.retrieveTaskDescription(task.getIdentifier().toString());

        assertTrue(result.isPresent());
        assertEquals("Test Task Description", result.get().getDescription());
        verify(taskRepository, times(1)).findById(task.getIdentifier());
    }

    @DisplayName("Fail to retrieve the task description by ID")
    @Test
    void retrieveTaskDescriptionByIdShouldReturnEmpty() {
        when(taskRepository.findById(task.getIdentifier())).thenReturn(Optional.empty());

        Optional<TaskDescription> result = taskService.retrieveTaskDescription(task.getIdentifier().toString());

        assertFalse(result.isPresent());
        verify(taskRepository, times(1)).findById(task.getIdentifier());
    }

    @DisplayName("Successfully retrieve the task description by namespace and version")
    @Test
    void retrieveTaskDescriptionByNamespaceAndVersionShouldReturnTaskDescription() {
        when(taskRepository.findByNamespaceAndVersion(task.getNamespace(), task.getVersion())).thenReturn(task);

        Optional<TaskDescription> result = taskService.retrieveTaskDescription(task.getNamespace(), task.getVersion());

        assertTrue(result.isPresent());
        assertEquals("Test Task Description", result.get().getDescription());
        verify(taskRepository, times(1)).findByNamespaceAndVersion(task.getNamespace(), task.getVersion());
    }

    @DisplayName("Fail to retrieve the task description by namespace and version")
    @Test
    void retrieveTaskDescriptionByNamespaceAndVersionShouldReturnEmpty() {
        when(taskRepository.findByNamespaceAndVersion(task.getNamespace(), task.getVersion())).thenReturn(null);

        Optional<TaskDescription> result = taskService.retrieveTaskDescription(task.getNamespace(), task.getVersion());

        assertFalse(result.isPresent());
        verify(taskRepository, times(1)).findByNamespaceAndVersion(task.getNamespace(), task.getVersion());
    }


    @DisplayName("Successfully create a task description")
    @Test
    void makeTaskDescriptionShouldReturnTaskDescription() {
        TaskDescription result = taskService.makeTaskDescription(task);

        assertNotNull(result);
        assertEquals(task.getIdentifier(), result.getId());
        assertEquals(task.getNamespace(), result.getNamespace());
        assertEquals(task.getVersion(), result.getVersion());
        assertEquals(task.getDescription(), result.getDescription());
    }

}
