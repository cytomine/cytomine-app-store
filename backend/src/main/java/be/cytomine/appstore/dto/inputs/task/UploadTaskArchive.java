package be.cytomine.appstore.dto.inputs.task;

import java.io.File;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import be.cytomine.appstore.utils.DescriptorHelper;

@Slf4j
@Setter
@Getter
@NoArgsConstructor
public class UploadTaskArchive {

    private File descriptorFile;

    private File dockerImage;

    private File logo;

    private JsonNode descriptorFileAsJson;

    public UploadTaskArchive(File descriptorFile, File dockerImage, File logoData) {
        this.descriptorFile = descriptorFile;
        this.dockerImage = dockerImage;
        this.descriptorFileAsJson = DescriptorHelper.parseDescriptor(descriptorFile);
        this.logo = logoData;
    }
}
