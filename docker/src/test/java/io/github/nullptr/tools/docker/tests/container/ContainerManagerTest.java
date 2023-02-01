package io.github.nullptr.tools.docker.tests.container;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.WaitResponse;
import io.github.nullptr.tools.docker.DockerManager;
import io.github.nullptr.tools.docker.container.ContainerManager;
import io.github.nullptr.tools.docker.utils.DockerHost;
import io.github.nullptr.tools.io.InstantFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Testcontainers
@DisplayName("ContainerManager")
public class ContainerManagerTest {

    private static final String STRING_DUMMY_IMAGE = "mongo:latest";
    private static final DockerImageName DUMMY_IMAGE_NAME = DockerImageName.parse(STRING_DUMMY_IMAGE);

    private static final ContainerManager CONTAINER_MANAGER = new DockerManager.Builder().withHost(DockerHost.UNIX_SOCKET).build().getContainerManager();

    @Test
    @DisplayName("List containers")
    public void testListContainers() {
        final GenericContainer<?> dummyContainer = new GenericContainer<>(DUMMY_IMAGE_NAME);
        dummyContainer.start();

        final List<Container> byName = CONTAINER_MANAGER.listContainers(dummyContainer.getContainerName(), null, null, null, false);
        Assertions.assertEquals(1, byName.size());

        final List<Container> byId = CONTAINER_MANAGER.listContainers(null, dummyContainer.getContainerId(), null, null, false);
        Assertions.assertEquals(1, byId.size());

        dummyContainer.stop();
    }

    @Test
    @DisplayName("Remove container")
    public void testRemoveContainer() {
        final GenericContainer<?> dummyContainer = new GenericContainer<>(DUMMY_IMAGE_NAME);
        dummyContainer.start();

        CONTAINER_MANAGER.removeContainer(dummyContainer.getContainerId(), true, true);

        final List<Container> byId = CONTAINER_MANAGER.listContainers(null, dummyContainer.getContainerId(), null, null, false);
        Assertions.assertEquals(0, byId.size());

        dummyContainer.stop();
    }

    @Test
    @DisplayName("Create container")
    public void testCreateContainer() {
        final CreateContainerResponse container = CONTAINER_MANAGER.createContainer(STRING_DUMMY_IMAGE, null, null);
        Assertions.assertAll(
                () -> Assertions.assertNotNull(container.getId()),
                () -> Assertions.assertNull(container.getWarnings())
        );

        CONTAINER_MANAGER.removeContainer(container.getId(), true, true);
    }

    @Test
    @DisplayName("Start and stop container")
    public void testStartAndStopContainer() {
        final CreateContainerResponse container = CONTAINER_MANAGER.createContainer(STRING_DUMMY_IMAGE, null, null);
        CONTAINER_MANAGER.stopContainer(container.getId(), 1000);

        CONTAINER_MANAGER.startContainer(container.getId());
        Assertions.assertEquals(Boolean.TRUE, CONTAINER_MANAGER.inspectContainer(container.getId()).getState().getRunning());

        CONTAINER_MANAGER.removeContainer(container.getId(), true, true);
    }

    @Test
    @DisplayName("Inspect container")
    public void testInspectContainer() {
        final CreateContainerResponse container = CONTAINER_MANAGER.createContainer(STRING_DUMMY_IMAGE, null, null);

        final InspectContainerResponse inspectContainer = CONTAINER_MANAGER.inspectContainer(container.getId());
        Assertions.assertAll(
                () -> Assertions.assertEquals(container.getId(), inspectContainer.getId()),
                () -> Assertions.assertEquals(STRING_DUMMY_IMAGE, inspectContainer.getImageId())
        );

        CONTAINER_MANAGER.removeContainer(container.getId(), true, true);
    }

    @Test
    @DisplayName("Wait container")
    public void testWaitContainer() {
        final CreateContainerResponse container = CONTAINER_MANAGER.createContainer(STRING_DUMMY_IMAGE, null, null);

        final WaitResponse response = CONTAINER_MANAGER.waitContainer(container.getId()).getLastResult();
        Assertions.assertEquals(0, response.getStatusCode());

        CONTAINER_MANAGER.removeContainer(container.getId(), true, true);
    }

    @Test
    @DisplayName("Copy file into and from container")
    public void testCopyWithContainer() {
        final CreateContainerResponse container = CONTAINER_MANAGER.createContainer(STRING_DUMMY_IMAGE, null, null);
        final Path path = Paths.get("test.txt");
        new InstantFile(path, "Test content for testing");

        CONTAINER_MANAGER.copyToContainer(container.getId(), "test.txt", "/test.txt", false, false);

        try (final InputStream stream = CONTAINER_MANAGER.copyFromContainer(container.getId(), "/test.txt", "new-test.txt")) {
            final String content = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining("\n"));
            Assertions.assertEquals("Test content for testing", content);

            Files.delete(path);
            Files.delete(Paths.get("new-test.txt"));
        } catch (final Exception e) {
            Assertions.fail(e);
        }

        CONTAINER_MANAGER.removeContainer(container.getId(), true, true);
    }
}
