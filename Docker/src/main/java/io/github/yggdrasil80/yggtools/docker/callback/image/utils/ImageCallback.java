package io.github.yggdrasil80.yggtools.docker.callback.image.utils;

import com.github.dockerjava.api.async.ResultCallback;
import io.github.yggdrasil80.yggtools.docker.callback.BaseCallback;

/**
 * The base class for callbacks that are used to process the results of an image operation.
 * @param <Callback> The type of the callback.
 * @param <Result> The type of the result.
 */
public abstract class ImageCallback<Callback extends ResultCallback<Result>, Result> extends BaseCallback<Callback, Result> {

    /**
     * The image id.
     */
    protected final String image;
    /**
     * The tag of the image.
     */
    protected final String tag;
    /**
     * The image, with the tag.
     */
    protected final String imageName;

    /**
     * The image callback constructor.
     * @param logger The name to use for the logger.
     * @param image The image id.
     * @param tag The tag of the image.
     * @param startMessage The message to use when starting the operation.
     * @param errorMessage The message to use when an error occurs.
     * @param finishMessage The message to use when the operation finishes.
     */
    public ImageCallback(String logger, String image, String tag, String startMessage, String errorMessage, String finishMessage) {
        super(logger, startMessage, errorMessage, finishMessage);

        this.image = image;
        this.tag = tag;
        this.imageName = image + (tag != null ? ":" + tag : "");

        this.startMessage = this.startMessage.replace("%image%", this.imageName);
        this.errorMessage = this.errorMessage.replace("%image%", this.imageName);
        this.finishMessage = this.finishMessage.replace("%image%", this.imageName);
    }

    /**
     * Get the image id.
     * @return The image id.
     */
    public String getImage() {
        return this.image;
    }

    /**
     * Get the tag of the image.
     * @return The tag of the image.
     */
    public String getTag() {
        return this.tag;
    }

    /**
     * Get the image, with the tag.
     * @return The image, with the tag.
     */
    public String getImageName() {
        return this.imageName;
    }
}
