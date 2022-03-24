package io.github.yggdrasil80.yggtools.docker.callback.image;

import com.github.dockerjava.api.model.PullResponseItem;
import io.github.yggdrasil80.yggtools.docker.callback.image.utils.ImageCallback;

/**
 * Callback for the pulling images.
 */
public class PullImageCallback extends ImageCallback<PullImageCallback, PullResponseItem> {

    /**
     * The image pull callback constructor.
     * @param image The image name.
     * @param tag The image tag.
     */
    public PullImageCallback(String image, String tag) {
        super("PullImageCallback", image, tag, "Pulling image: %image%", "pull image: %image%", "pulled image: %image%");
    }
}
