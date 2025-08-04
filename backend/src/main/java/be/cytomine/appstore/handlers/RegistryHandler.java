package be.cytomine.appstore.handlers;

import be.cytomine.appstore.dto.handlers.registry.DockerImage;
import be.cytomine.appstore.exceptions.RegistryException;

public interface RegistryHandler {
    boolean checkImage(DockerImage image) throws RegistryException;

    void pushImage(DockerImage image) throws RegistryException;
}
