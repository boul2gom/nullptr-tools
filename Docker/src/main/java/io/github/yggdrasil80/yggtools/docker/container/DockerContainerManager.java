package io.github.yggdrasil80.yggtools.docker.container;

import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import io.github.yggdrasil80.yggtools.docker.DockerManager;
import io.github.yggdrasil80.yggtools.docker.callback.container.StatsContainerCallback;
import io.github.yggdrasil80.yggtools.docker.callback.container.WaitContainerCallback;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * The manager for docker containers.
 */
public class DockerContainerManager {

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerContainerManager.class);

    /**
     * The docker manager.
     */
    private final DockerManager manager;

    /**
     * The docker container manager constructor.
     * @param manager The manager, to get Docker client instance.
     */
    public DockerContainerManager(DockerManager manager) {
        this.manager = manager;
    }

    /**
     * List containers, filtered with the given filters, if any. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param name The name of the container (optional).
     * @param id The id of the container (optional).
     * @param volume The volume of the container (optional).
     * @param network The network of the container (optional).
     * @param showAll Show all containers (optional).
     * @return The list of containers.
     */
    public List<Container> listContainers(String name, String id, String volume, String network, Boolean showAll) {
        return this.listContainers(null, name, id, null, volume, network, null, null, null, null, showAll, null);
    }

    /**
     * List containers, filtered with the given filters, if any. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param beforeId All containers created before this container id (optional).
     * @param name All containers with this name (optional).
     * @param id All containers with this id (optional).
     * @param ancestor Containers created by this image or its parents (optional).
     * @param volume All containers with this volume (optional).
     * @param network All containers with this network (optional).
     * @param exitCode All containers with this exit code (optional).
     * @param label All containers with this label (optional).
     * @param since All containers created after this container id (optional).
     * @param showSize Show the size of the container (optional).
     * @param showAll Show all containers (optional).
     * @param limit Limit the number of containers returned (optional).
     * @return The list of containers.
     */
    public List<Container> listContainers(String beforeId, String name, String id, String ancestor, String volume,
        String network, Integer exitCode, String label, String since, Boolean showSize, Boolean showAll, Integer limit) {
        final ListContainersCmd cmd = this.manager.getClient().listContainersCmd();

        if (beforeId != null) cmd.withBefore(beforeId);
        if (name != null) cmd.withNameFilter(Collections.singleton(name));
        if (id != null) cmd.withIdFilter(Collections.singleton(id));
        if (ancestor != null) cmd.withAncestorFilter(Collections.singleton(ancestor));
        if (volume != null) cmd.withVolumeFilter(Collections.singleton(volume));
        if (network != null) cmd.withNetworkFilter(Collections.singleton(network));
        if (exitCode != null) cmd.withExitedFilter(exitCode);
        if (label != null) cmd.withLabelFilter(Collections.singleton(label));
        if (since != null) cmd.withSince(since);
        if (showSize != null) cmd.withShowSize(showSize);
        if (showAll != null) cmd.withShowAll(showAll);
        if (limit != null) cmd.withLimit(limit);

        LOGGER.info("Listing containers...");
        return cmd.exec();
    }

    /**
     * Create a container, without starting it. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param image The image to use.
     * @param name The name of the container (optional).
     * @param env The environment variables to set in the container (optional).
     * @return The created container.
     */
    public CreateContainerResponse createContainer(@NotNull String image, String name, List<String> env) {
        return this.createContainer(image, name, null, null, env, null);
    }

    /**
     * Create a container, without starting it. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param image The image to use.
     * @param name The name of the container (optional).
     * @param command The command to execute in the container (optional).
     * @param volumes The volumes to mount in the container (optional).
     * @param env The environment variables to set in the container (optional).
     * @param config The configuration to use for the container, like ports binding or material resources (optional).
     * @return The created container.
     */
    public CreateContainerResponse createContainer(@NotNull String image, String name, String command, List<Volume> volumes, List<String> env, HostConfig config) {
        final CreateContainerCmd cmd = this.manager.getClient().createContainerCmd(image);

        if (name != null) cmd.withName(name);
        if (command != null) cmd.withCmd(command);
        if (volumes != null) cmd.withVolumes(volumes);
        if (env != null) cmd.withEnv(env);
        if (config != null) cmd.withHostConfig(config);

        LOGGER.info("Creating container " + image + "...");
        return cmd.exec();
    }

    /**
     * Start a previously created container.
     * @param id The id of the container to start.
     */
    public void startContainer(@NotNull String id) {
        LOGGER.info("Starting container " + id + "...");
        this.manager.getClient().startContainerCmd(id).exec();
    }

    /**
     * Create a container and automatically start it. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param image The image to use.
     * @param name The name of the container (optional).
     * @param env The environment variables to set in the container (optional).
     */
    public void runContainer(@NotNull String image, String name, List<String> env) {
        this.runContainer(image, name, null, null, env, null);
    }

    /**
     * Create a container and automatically start it. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param image The image to use.
     * @param name The name of the container (optional).
     * @param command The command to execute in the container (optional).
     * @param volumes The volumes to mount in the container (optional).
     * @param env The environment variables to set in the container (optional).
     * @param config The configuration to use for the container, like ports binding or material resources (optional).
     */
    public void runContainer(@NotNull String image, String name, String command, List<Volume> volumes, List<String> env, HostConfig config) {
        final CreateContainerResponse response = this.createContainer(image, name, command, volumes, env, config);
        this.startContainer(response.getId());

        LOGGER.info("Container " + response.getId() + " created with image " + image + " as " + name + ".");
    }

    /**
     * Inspect a container.
     * @param containerId The id of the container to inspect.
     * @return The container.
     */
    public InspectContainerResponse inspectContainer(@NotNull String containerId) {
        LOGGER.info("Inspecting container " + containerId + "...");
        return this.manager.getClient().inspectContainerCmd(containerId).exec();
    }

    /**
     * Wait for a container to finish.
     * @param containerId The id of the container to wait for.
     * @return The wait response.
     */
    public WaitContainerCallback waitContainer(@NotNull String containerId) {
        try {
            return this.manager.getClient().waitContainerCmd(containerId).exec(new WaitContainerCallback(containerId)).awaitCompletion();
        } catch (InterruptedException e) {
            LOGGER.error("An error has occurred while waiting for container " + containerId + ".", e);
            return null;
        }
    }

    /**
     * Copy a file from a container to the host. <br>
     * /!\ Warning: Don't forget to close the stream after usage to avoid memory leaks.
     * @param containerId The id of the container to copy from.
     * @param resourcePath The path of the resource to copy.
     * @param destination The destination to copy the resource to.
     * @return The stream to read the resource.
     */
    public InputStream copyFromContainer(@NotNull String containerId, @NotNull String resourcePath, @NotNull String destination) {
        LOGGER.info("Copying resource " + resourcePath + " from container " + containerId + " to " + destination + "...");
        return this.manager.getClient().copyArchiveFromContainerCmd(containerId, resourcePath).withHostPath(destination).exec();
    }

    /**
     * Copy a file from the host to a container. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param containerId The id of the container to copy to.
     * @param resourcePath The path of the resource to copy.
     * @param destination The destination to copy the resource to.
     * @param noOverwriteDirNonDir <code>true</code> to prevent overwriting of directory names with files when copying, and vice versa (optional).
     * @param dirChildrenOnly <code>true</code> to copy only the directory's children, and not the directory itself (optional).
     */
    public void copyToContainer(@NotNull String containerId, @NotNull String resourcePath, @NotNull String destination,
                                Boolean noOverwriteDirNonDir, Boolean dirChildrenOnly) {
        final CopyArchiveToContainerCmd cmd = this.manager.getClient().copyArchiveToContainerCmd(containerId)
                .withHostResource(resourcePath).withRemotePath(destination);

        if (noOverwriteDirNonDir != null) cmd.withNoOverwriteDirNonDir(noOverwriteDirNonDir);
        if (dirChildrenOnly != null) cmd.withDirChildrenOnly(dirChildrenOnly);

        LOGGER.info("Copying resource " + resourcePath + " to container " + containerId + " to " + destination + "...");
        cmd.exec();
    }

    /**
     * Get files and directories changes from a container.
     * @param containerId The id of the container to get changes from.
     * @return The changes.
     */
    public List<ChangeLog> getContainerDiff(@NotNull String containerId) {
        LOGGER.info("Getting container " + containerId + " changes...");
        return this.manager.getClient().containerDiffCmd(containerId).exec();
    }

    /**
     * Properly stop a container. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param containerId The id of the container to stop.
     * @param timeout The timeout to wait for the container to stop (optional).
     */
    public void stopContainer(@NotNull String containerId, Integer timeout) {
        final StopContainerCmd cmd = this.manager.getClient().stopContainerCmd(containerId);

        if (timeout != null) cmd.withTimeout(timeout);

        LOGGER.info("Stopping container " + containerId + "...");
        cmd.exec();
    }

    /**
     * Remove a previously stopped container.
     * @param containerId The id of the container to remove.
     * @param removeVolumes <code>true</code> to remove the volumes associated with the container, <code>false</code> otherwise (optional).
     * @param force <code>true</code> to force the removal of a running container, <code>false</code> otherwise (optional).
     */
    public void removeContainer(@NotNull String containerId, Boolean removeVolumes, Boolean force) {
        final RemoveContainerCmd cmd = this.manager.getClient().removeContainerCmd(containerId);

        if (removeVolumes != null) cmd.withRemoveVolumes(removeVolumes);
        if (force != null) cmd.withForce(force);

        LOGGER.info("Removing container " + containerId + "...");
        cmd.exec();
    }

    /**
     * Force stops a container. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param containerId The id of the container to stop.
     * @param signal The signal to send to the container (optional).
     */
    public void killContainer(@NotNull String containerId, String signal) {
        final KillContainerCmd cmd = this.manager.getClient().killContainerCmd(containerId);

        if (signal != null) cmd.withSignal(signal);

        LOGGER.info("Killing container " + containerId + "...");
        cmd.exec();
    }

    /**
     * Rename a container.
     * @param containerId The id of the container to rename.
     * @param name The new name of the container.
     */
    public void renameContainer(@NotNull String containerId, @NotNull String name) {
        LOGGER.info("Renaming container " + containerId + " to " + name + "...");
        this.manager.getClient().renameContainerCmd(containerId).withName(name).exec();
    }

    /**
     * Restart a container. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param containerId The id of the container to restart.
     * @param timeout The timeout to wait for the container to restart (optional).
     */
    public void restartContainer(@NotNull String containerId, Integer timeout) {
        final RestartContainerCmd cmd = this.manager.getClient().restartContainerCmd(containerId);

        if (timeout != null) cmd.withTimeout(timeout);

        LOGGER.info("Restarting container " + containerId + "...");
        cmd.exec();
    }

    /**
     * List processes running inside a container. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param containerId The id of the container to list processes for.
     * @param psArgs The arguments to pass to ps (optional).
     * @return The processes.
     */
    public TopContainerResponse listProcesses(@NotNull String containerId, String psArgs) {
        final TopContainerCmd cmd = this.manager.getClient().topContainerCmd(containerId);

        if (psArgs != null) cmd.withPsArgs(psArgs);

        LOGGER.info("Listing processes for container " + containerId + "...");
        return cmd.exec();
    }

    /**
     * Pause a container.
     * @param containerId The id of the container to pause.
     */
    public void pauseContainer(@NotNull String containerId) {
        LOGGER.info("Pausing container " + containerId + "...");
        this.manager.getClient().pauseContainerCmd(containerId).exec();
    }

    /**
     * Unpause a container.
     * @param containerId The id of the container to unpause.
     */
    public void resumeContainer(@NotNull String containerId) {
        LOGGER.info("Resuming container " + containerId + "...");
        this.manager.getClient().unpauseContainerCmd(containerId).exec();
    }

    /**
     * Get stats about a container.
     * @param containerId The id of the container to get stats for.
     * @return The stats.
     */
    public StatsContainerCallback getContainerStats(@NotNull String containerId) {
        try {
            return this.manager.getClient().statsCmd(containerId).withNoStream(false).exec(new StatsContainerCallback(containerId)).awaitCompletion();
        } catch (InterruptedException e) {
            LOGGER.error("An error has occurred while retrieving stats for container " + containerId + ".", e);
            return null;
        }
    }
}
