package io.github.nullptr.tools.docker.callback.container;

import com.github.dockerjava.api.model.WaitResponse;
import io.github.nullptr.tools.docker.callback.container.utils.ContainerCallback;

/**
 * Callback for waiting for a container to finish.
 */
public class WaitContainerCallback extends ContainerCallback<WaitContainerCallback, WaitResponse> {

    /**
     * The container waiting callback constructor.
     * @param containerId The container id.
     */
    public WaitContainerCallback(String containerId) {
        super("WaitContainerCallback", containerId, "Waiting container %id%", "wait container %id%",
                "waited container %id%");
    }
}
