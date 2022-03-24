package io.github.yggdrasil80.yggtools.docker.volume;

import com.github.dockerjava.api.command.*;
import io.github.yggdrasil80.yggtools.docker.DockerManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

/**
 * Manager for docker volumes.
 */
public class DockerVolumeManager {

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerVolumeManager.class);

    /**
     * The docker manager.
     */
    private final DockerManager manager;

    /**
     * The docker volume manager constructor.
     * @param manager The docker manager.
     */
    public DockerVolumeManager(DockerManager manager) {
        this.manager = manager;
    }

    /**
     * Create a docker volume. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param name The name of the volume (optional).
     * @param driver The driver of the volume (optional).
     * @param driverOpts The driver options of the volume (optional).
     * @return The created volume.
     */
    public CreateVolumeResponse createVolume(String name, String driver, Map<String, String> driverOpts) {
        final CreateVolumeCmd cmd = this.manager.getClient().createVolumeCmd();

        if (name != null) cmd.withName(name);
        if (driver != null) cmd.withDriver(driver);
        if (driverOpts != null) cmd.withDriverOpts(driverOpts);

        LOGGER.info("Creating volume" + (name != null ? " " + name : "") + "...");
        return cmd.exec();
    }

    /**
     * Inspect a docker volume.
     * @param name The name of the volume.
     * @return The inspected volume.
     */
    public InspectVolumeResponse inspectVolume(@NotNull String name) {
        LOGGER.info("Inspecting volume " + name + "...");
        return this.manager.getClient().inspectVolumeCmd(name).exec();
    }

    /**
     * Remove a docker volume.
     * @param name The name of the volume.
     */
    public void removeVolume(@NotNull String name) {
        LOGGER.info("Removing volume " + name + "...");
        this.manager.getClient().removeVolumeCmd(name).exec();
    }

    /**
     * List all docker volumes, filtered by given filters if any. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param name The name of the volume (optional).
     * @param driver The driver of the volume (optional).
     * @param dangling <code>true</code> if only volumes not used by containers should be returned, <code>false</code> otherwise (optional).
     * @return The list of volumes.
     */
    public ListVolumesResponse listVolumes(String name, String driver, Boolean dangling) {
        final ListVolumesCmd cmd = this.manager.getClient().listVolumesCmd();

        if (name != null) cmd.withFilter("name", Collections.singleton(name));
        if (driver != null) cmd.withFilter("driver", Collections.singleton(driver));
        if (dangling != null) cmd.withDanglingFilter(dangling);

        LOGGER.info("Listing volumes...");
        return cmd.exec();
    }
}
