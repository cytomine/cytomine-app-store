package be.cytomine.appstore.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.core.io.ClassPathResource;

import be.cytomine.appstore.dto.inputs.task.UploadTaskArchive;
import be.cytomine.appstore.handlers.StorageData;
import be.cytomine.appstore.handlers.StorageDataEntry;
import be.cytomine.appstore.handlers.StorageDataType;
import be.cytomine.appstore.models.task.Author;
import be.cytomine.appstore.models.task.Parameter;
import be.cytomine.appstore.models.task.ParameterType;
import be.cytomine.appstore.models.task.Task;
import be.cytomine.appstore.models.task.Type;

public class TaskUtils {
    public static UploadTaskArchive createTestUploadTaskArchive() throws IOException {
        File descriptorFile = new ClassPathResource("artifacts/descriptor.yml").getFile();

        // Create a copy because it will be deleted after the upload process
        Path original = new ClassPathResource("artifacts/image.tar").getFile().toPath();
        Path copy = Path.of("src/test/resources/artifacts/docker-test-image.tar");
        Files.copy(original, copy, StandardCopyOption.REPLACE_EXISTING);
        File dockerImage = copy.toFile();
        dockerImage.deleteOnExit();

        return new UploadTaskArchive(descriptorFile, dockerImage);
    }

    public static Author createTestAuthor() {
        Author author = new Author();
        author.setFirstName("Cytomine");
        author.setLastName("ULiege");
        author.setOrganization("University of Liege");
        author.setEmail("cytomine@uliege.be");
        author.setContact(true);

        return author;
    }

    public static Parameter createTestInput(String name, boolean binaryType) {
        Type type = new Type();
        type.setCharset("UTF-8");

        Parameter input = new Parameter();
        input.setName(name);
        input.setDisplayName("Input");
        input.setDescription("Input description");
        input.setOptional(false);
        input.setType(type);
        input.setParameterType(ParameterType.INPUT);

        return input;
    }

    public static Parameter createTestOutput(String name, boolean binaryType) {
        Type type = new Type();
        type.setCharset("UTF-8");

        Parameter output = new Parameter();
        output.setName(name);
        output.setDisplayName("Output");
        output.setDescription("output description");
        output.setOptional(false);
        output.setType(type);
        output.setParameterType(ParameterType.OUTPUT);

        return output;
    }

    public static Task createTestTask(boolean binaryType) {
        Task task = new Task();
        task.setIdentifier(UUID.randomUUID());
        task.setNamespace("namespace");
        task.setVersion("version");
        task.setStorageReference("storageReference");
        task.setDescription("Test Task Description");
        task.setAuthors(Set.of(createTestAuthor()));
        task.setParameters(Set.of(createTestInput("name", binaryType), createTestOutput("out", binaryType)));

        return task;
    }

    public static Task createTestTaskWithMultipleIO() {
        Task task = new Task();
        task.setIdentifier(UUID.randomUUID());
        task.setNamespace("namespace");
        task.setVersion("version");
        task.setStorageReference("storageReference");
        task.setDescription("Test Task Description");
        task.setAuthors(Set.of(createTestAuthor()));
        task.setParameters(Set.of(createTestInput("name 1", false),
            createTestInput("name 2", false),
            createTestOutput("out 1", false),
            createTestOutput("out 2", false)));

        return task;
    }


    public static StorageData createTestStorageData(String parameterName, String storageId) throws IOException {
        File data = File.createTempFile("data", null);
        data.deleteOnExit();

        return new StorageData(new StorageDataEntry(data, parameterName, storageId, StorageDataType.FILE));
    }

    public static byte[] createFakeOutputsZip(String... names) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos)) {

            for (String name : names) {
                zos.putNextEntry(new ZipEntry(name));
                zos.write("42".getBytes());
                zos.closeEntry();
            }

            zos.finish();
            return baos.toByteArray();
        }
    }

    public static Task createTask(
        UUID uuid,
        String iamgeNameSpace,
        String name,
        String shortName,
        String version,
        String storagereference,
        String desc,
        String authorFirstName,
        String authorLastName,
        String org,
        String mail) {

        Task task = new Task();
        task.setIdentifier(uuid);
        task.setNamespace(iamgeNameSpace);
        task.setName(name);
        task.setNameShort(shortName);
        task.setVersion(version);
        task.setStorageReference(storagereference);
        task.setDescription(desc);

        Author author = new Author();
        author.setFirstName(authorFirstName);
        author.setLastName(authorLastName);
        author.setOrganization(org);
        author.setEmail(mail);
        author.setContact(true);

        task.setAuthors(Set.of(author));
        task.setParameters(
            Set.of(createTestInput("input", false), createTestOutput("output", false)));

        return task;
    }
}
