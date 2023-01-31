package io.github.nullptr.tools.docker.tests.utils;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class DockerContainer extends GenericContainer<DockerContainer> {

    private static final DockerImageName DOCKER_IMAGE_NAME = DockerImageName.parse("docker:latest");

    public DockerContainer() {
        super(DOCKER_IMAGE_NAME);

        super.withExposedPorts(2375, 2376);
    }
}
