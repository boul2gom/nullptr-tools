package io.github.nullptr.tools.docker.callback.image;

import com.github.dockerjava.api.model.BuildResponseItem;
import io.github.nullptr.tools.docker.callback.BaseCallback;

import java.io.File;

/**
 * Callback for build image.
 */
public class BuildImageCallback extends BaseCallback<BuildImageCallback, BuildResponseItem> {

    /**
     * The image building callback constructor.
     * @param dockerfile The dockerfile.
     */
    public BuildImageCallback(File dockerfile) {
        super("BuildImageCallback", "Building image from: " + dockerfile.getAbsolutePath(),
                "build image from: " + dockerfile.getAbsolutePath(), "built image from: " + dockerfile.getAbsolutePath());
    }
}
