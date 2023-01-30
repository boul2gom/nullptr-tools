package io.github.nullptr.tools.docker.callback.container;

import com.github.dockerjava.api.model.Statistics;
import io.github.nullptr.tools.docker.callback.container.utils.ContainerCallback;

/**
 * Callback for container statistics.
 */
public class StatsContainerCallback extends ContainerCallback<StatsContainerCallback, Statistics> {

    /**
     * The container statistics callback constructor.
     * @param containerId The container id.
     */
    public StatsContainerCallback(String containerId) {
        super("StatsContainerCallback", containerId, "Retrieving stats for container %id%",
                "retrieve stats for container %id%", "retrieved stats for container %id%");
    }
}
