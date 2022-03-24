package io.github.yggdrasil80.yggtools.docker.callback.container.utils;

import com.github.dockerjava.api.async.ResultCallback;
import io.github.yggdrasil80.yggtools.docker.callback.BaseCallback;

/**
 * The base class for callbacks that are used to handle the result of a container operation.
 * @param <Callback> The type of the callback.
 * @param <Result> The type of the result.
 */
public abstract class ContainerCallback<Callback extends ResultCallback<Result>, Result> extends BaseCallback<Callback, Result> {

    /**
     * The container ID.
     */
    protected final String containerId;

    /**
     * The container callback constructor.
     * @param logger The name to use for the logger.
     * @param containerId The container ID.
     * @param startMessage The message to use when starting the operation.
     * @param errorMessage The message to use when an error occurs.
     * @param finishMessage The message to use when finishing the operation.
     */
    public ContainerCallback(String logger, String containerId, String startMessage, String errorMessage, String finishMessage) {
        super(logger, startMessage, errorMessage, finishMessage);

        this.containerId = containerId;

        this.startMessage = this.startMessage.replace("%id%", this.containerId);
        this.errorMessage = this.errorMessage.replace("%id%", this.containerId);
        this.finishMessage = this.finishMessage.replace("%id%", this.containerId);
    }

    /**
     * Get the container ID.
     * @return The container ID.
     */
    public String getContainerId() {
        return this.containerId;
    }
}
