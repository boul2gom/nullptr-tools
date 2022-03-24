package io.github.yggdrasil80.yggtools.docker.callback.image;

import com.github.dockerjava.api.model.PushResponseItem;
import io.github.yggdrasil80.yggtools.docker.callback.image.utils.ImageCallback;

/**
 * Callback for push image.
 */
public class PushImageCallback extends ImageCallback<PushImageCallback, PushResponseItem> {

    /**
     * The image push callback constructor.
     * @param image The image name.
     * @param tag The image tag.
     */
    public PushImageCallback(String image, String tag) {
        super("PushImageCallback", image, tag, "Pushing image: %image%", "push image: %image%", "pushed image: %image%");
    }
}
