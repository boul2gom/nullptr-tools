package io.github.nullptr.tools.docker.tests.container;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.WaitResponse;
import io.github.nullptr.tools.docker.DockerManager;
import io.github.nullptr.tools.docker.container.ContainerManager;
import io.github.nullptr.tools.docker.tests.helper.ContainerHelper;
import io.github.nullptr.tools.docker.utils.DockerHost;
import io.github.nullptr.tools.io.InstantFile;
import io.github.nullptr.tools.list.ListHelper;
import io.github.nullptr.tools.platform.PlatformHelper;
import io.github.nullptr.tools.platform.PlatformOS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
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

    private static final DockerImageName DUMMY_IMAGE_NAME = DockerImageName.parse("alpine:latest");

    private static final ContainerManager CONTAINER_MANAGER;

    static {
        final DockerHost host = PlatformHelper.isOn(PlatformOS.WINDOWS) ? DockerHost.TCP_DAEMON : DockerHost.UNIX_SOCKET;

        CONTAINER_MANAGER = new DockerManager.Builder().withHost(host).build().getContainerManager();
    }

    @Test
    @DisplayName("List containers")
    public void testListContainers() {
        ContainerHelper.withTestContainer(DUMMY_IMAGE_NAME, container ->
            {
                final String containerId = container.getContainerId();
                final List<Container> byId = CONTAINER_MANAGER.listContainers(null, containerId, null, null, true);
                Assertions.assertTrue(ListHelper.contains(byId, c -> c.getId().contains(containerId)));

                final String containerName = container.getContainerName();
                final List<Container> byName = CONTAINER_MANAGER.listContainers(containerName, null, null, null, true);
                Assertions.assertTrue(ListHelper.contains(byName, c -> c.getNames()[0].contains(containerName)));
            }
        );
    }

    @Test
    @DisplayName("Remove container")
    public void testRemoveContainer() {
        ContainerHelper.withTestContainer(DUMMY_IMAGE_NAME, container ->
            {
                final String containerId = container.getContainerId();
                CONTAINER_MANAGER.stopContainer(containerId, 1000);
                CONTAINER_MANAGER.removeContainer(containerId, true, true);

                final List<Container> byId = CONTAINER_MANAGER.listContainers(null, containerId, null, null, false);
                Assertions.assertFalse(ListHelper.contains(byId, c -> c.getId().contains(containerId)));
            }
        );
    }

    @Test
    @DisplayName("Create container")
    public void testCreateContainer() {
        ContainerHelper.withContainer(CONTAINER_MANAGER, DUMMY_IMAGE_NAME, container ->
            {
                final String containerId = container.getId();
                final List<Container> byId = CONTAINER_MANAGER.listContainers(null, containerId, null, null, true);

                Assertions.assertTrue(ListHelper.contains(byId, c -> c.getId().contains(containerId)));
            }
        );
    }

    @Test
    @DisplayName("Start and stop container")
    public void testStartAndStopContainer() {
        ContainerHelper.withContainer(CONTAINER_MANAGER, DUMMY_IMAGE_NAME, container ->
                {
                    final String containerId = container.getId();
                    CONTAINER_MANAGER.stopContainer(containerId, 0);

                    CONTAINER_MANAGER.startContainer(container.getId());
                    Assertions.assertEquals(Boolean.TRUE, CONTAINER_MANAGER.inspectContainer(container.getId()).getState().getRunning());
                }
        );
    }

    @Test
    @DisplayName("Inspect container")
    public void testInspectContainer() {
        ContainerHelper.withContainer(CONTAINER_MANAGER, DUMMY_IMAGE_NAME, container ->
            {
                final InspectContainerResponse inspectContainer = CONTAINER_MANAGER.inspectContainer(container.getId());
                Assertions.assertAll(
                        () -> Assertions.assertEquals(container.getId(), inspectContainer.getId()),
                        () -> Assertions.assertEquals(DUMMY_IMAGE_NAME.getUnversionedPart(), inspectContainer.getImageId())
                );
            }
        );
    }

    @Test
    @DisplayName("Wait container")
    public void testWaitContainer() {
        ContainerHelper.withContainer(CONTAINER_MANAGER, DUMMY_IMAGE_NAME, container ->
            {
                final WaitResponse response = CONTAINER_MANAGER.waitContainer(container.getId()).getLastResult();
                LoggerFactory.getLogger(getClass()).info("Exit code: {}", response.getStatusCode());
                Assertions.assertEquals(0, response.getStatusCode());
            }
        );
    }

    @Test
    @DisplayName("Copy file into and from container")
    public void testCopyWithContainer() {
        ContainerHelper.withContainer(CONTAINER_MANAGER, DUMMY_IMAGE_NAME, container ->
            {
                final Path path = Paths.get("test.txt");
                try (final InstantFile file = new InstantFile(path, "Test content for testing")) {
                    file.write();
                }

                CONTAINER_MANAGER.copyToContainer(container.getId(), "test.txt", "/test.txt", false, false);

                try (final InputStream stream = CONTAINER_MANAGER.copyFromContainer(container.getId(), "/test.txt", "new-test.txt")) {
                    final String content = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining("\n"));
                    Assertions.assertEquals("Test content for testing", content);
                } catch (final Exception e) {
                    Assertions.fail(e);
                }

                try {
                    Files.delete(path);
                    Files.delete(Paths.get("new-test.txt"));
                } catch (final Exception e) {
                    Assertions.fail(e);
                }
            }
        );
    }
}
