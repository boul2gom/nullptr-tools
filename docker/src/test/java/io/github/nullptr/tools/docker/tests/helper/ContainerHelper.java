package io.github.nullptr.tools.docker.tests.helper;

import com.github.dockerjava.api.command.CreateContainerResponse;
import io.github.nullptr.tools.docker.container.ContainerManager;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.startupcheck.OneShotStartupCheckStrategy;
import org.testcontainers.utility.DockerImageName;

public class ContainerHelper {

    public static void withTestContainer(DockerImageName imageName, TestContainerConsumer consumer) {
        try (final GenericContainer<?> container = new GenericContainer<>(imageName)) {
            container.withStartupCheckStrategy(new OneShotStartupCheckStrategy());
            container.withCommand("tail", "-f", "/dev/null");
            container.start();

            consumer.accept(container);

            container.stop();
        }
    }

    public static void withContainer(ContainerManager manager, DockerImageName imageName, ContainerConsumer consumer) {
        final CreateContainerResponse response = manager.createContainer(imageName.asCanonicalNameString(), null, "tail -f /dev/null", null, null, null);
        final String containerId = response.getId();
        manager.startContainer(containerId);

        consumer.accept(response);

        manager.stopContainer(containerId, 0);
        manager.removeContainer(containerId, true, true);
    }
}
