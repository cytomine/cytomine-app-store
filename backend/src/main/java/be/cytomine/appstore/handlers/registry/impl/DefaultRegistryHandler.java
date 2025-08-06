package be.cytomine.appstore.handlers.registry.impl;

import java.io.OutputStream;

import be.cytomine.appstore.dto.handlers.registry.DockerImage;
import be.cytomine.appstore.exceptions.RegistryException;
import be.cytomine.appstore.handlers.RegistryHandler;

public class DefaultRegistryHandler implements RegistryHandler {
    @Override
    public boolean checkImage(DockerImage image) throws RegistryException {
        return false;
    }

    @Override
    public void pushImage(DockerImage image) throws RegistryException {

    }

    @Override
    public void pullImage(String imageName, OutputStream outputStream) throws RegistryException
    {

    }

}
