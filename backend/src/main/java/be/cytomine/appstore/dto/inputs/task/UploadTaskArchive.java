package be.cytomine.appstore.dto.inputs.task;

import java.io.File;

import be.cytomine.appstore.utils.DescriptorHelper;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
@NoArgsConstructor
public class UploadTaskArchive {

    private File descriptorFile;

    private File dockerImage;

    private JsonNode descriptorFileAsJson;

    public UploadTaskArchive(File descriptorFile, File dockerImage) {
        this.descriptorFile = descriptorFile;
        this.dockerImage = dockerImage;
        this.descriptorFileAsJson = DescriptorHelper.parseDescriptor(descriptorFile);
    }
}
