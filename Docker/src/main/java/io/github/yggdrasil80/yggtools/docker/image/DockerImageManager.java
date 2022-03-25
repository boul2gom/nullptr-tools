package io.github.yggdrasil80.yggtools.docker.image;

import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.SearchItem;
import io.github.yggdrasil80.yggtools.docker.DockerManager;
import io.github.yggdrasil80.yggtools.docker.callback.image.BuildImageCallback;
import io.github.yggdrasil80.yggtools.docker.callback.image.PullImageCallback;
import io.github.yggdrasil80.yggtools.docker.callback.image.PushImageCallback;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Manager for Docker images.
 */
public class DockerImageManager {

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerImageManager.class);

    /**
     * The Docker manager.
     */
    private final DockerManager manager;

    /**
     * The Docker image manager constructor.
     * @param manager The manager, to get Docker client instance.
     */
    public DockerImageManager(DockerManager manager) {
        this.manager = manager;
    }

    /**
     * Pulls an image from a registry, Docker Hub by default. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param image The image to pull.
     * @param tag The tag of the image to pull (optional).
     * @return The callback.
     */
    public PullImageCallback pullImage(@NotNull String image, String tag) {
        return this.pullImage(image, tag, null, null, null);
    }

    /**
     * Pulls an image from a registry, Docker Hub by default. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param image The image to pull.
     * @param tag The tag of the image to pull (optional).
     * @param platform The platform of the image to pull (optional).
     * @param registry The registry to pull the image from (optional).
     * @param auth The authentication to use (optional).
     * @return The callback.
     */
    public PullImageCallback pullImage(@NotNull String image, String tag, String platform, String registry, AuthConfig auth) {
        try {
            final PullImageCmd cmd = this.manager.getClient().pullImageCmd(image);

            if (tag != null) cmd.withTag(tag);
            if (platform != null) cmd.withPlatform(platform);
            if (registry != null) cmd.withRegistry(registry);
            if (auth != null) cmd.withAuthConfig(auth);

            return cmd.exec(new PullImageCallback(image, tag)).awaitCompletion();
        } catch (InterruptedException e) {
            LOGGER.error("An error has occurred when pulling image: " + image + (tag != null ? ":" + tag : "") + " !", e);
            return null;
        }
    }

    /**
     * Push an image to a registry. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param image The image to push, with the repository and the registry if needed. <br>
     * Example: "ghcr.io/yggdrasil80/my-image" for custom registry, "yggdrasil80/my-image" or "my-image" if the registry is Docker Hub.
     * @param tag The tag of the image to push (optional).
     * @param auth The authentication to use (optional).
     * @return The callback.
     */
    public PushImageCallback pushImage(@NotNull String image, String tag, AuthConfig auth) {
        try {
            final PushImageCmd cmd = this.manager.getClient().pushImageCmd(image);

            if (tag != null) cmd.withTag(tag);
            if (auth != null) cmd.withAuthConfig(auth);

            return cmd.exec(new PushImageCallback(image, tag)).awaitCompletion();
        } catch (InterruptedException e) {
            LOGGER.error("An error has occurred when pushing image: " + image + (tag != null ? ":" + tag : "") + " !", e);
            return null;
        }
    }

    /**
     * Load an image from a tar archive.
     * @param tarImageFile The tar archive to load.
     */
    public void loadImage(@NotNull File tarImageFile) {
        try {
            LOGGER.info("Loading image: " + tarImageFile.getAbsolutePath() + "...");
            this.manager.getClient().loadImageCmd(Files.newInputStream(tarImageFile.toPath())).exec();
        } catch (IOException e) {
            LOGGER.error("An error has occurred when loading image: " + tarImageFile.getAbsolutePath() + ".", e);
        }
    }

    /**
     * Search for an image. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param term The term to search for.
     * @param limit The maximum number of results to return (optional).
     * @return The list of results.
     */
    public List<SearchItem> searchImages(@NotNull String term, Integer limit) {
        final SearchImagesCmd cmd = this.manager.getClient().searchImagesCmd(term);

        if (limit != null) cmd.withLimit(limit);

        LOGGER.info("Searching images for term: " + term + "...");
        return cmd.exec();
    }

    /**
     * Remove a pulled image. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param image The image to remove, with the repository and the registry if needed.
     * @param force Force the removal of the image (optional).
     * @param dontRemoveParents <code>true</code> to don't remove the parent images (optional).
     */
    public void removeImage(@NotNull String image, Boolean force, Boolean dontRemoveParents) {
        final RemoveImageCmd cmd = this.manager.getClient().removeImageCmd(image);

        if (force != null) cmd.withForce(force);
        if (dontRemoveParents != null) cmd.withNoPrune(dontRemoveParents);

        LOGGER.info("Removing image: " + image + "...");
        cmd.exec();
    }

    /**
     * List pulled images. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param showAll <code>true</code> to show all images (optional).
     * @param nameFilter The name filter to use (optional).
     * @return The list of pulled images.
     */
    public List<Image> listImages(Boolean showAll, String nameFilter) {
        final ListImagesCmd cmd = this.manager.getClient().listImagesCmd();

        if (showAll != null) cmd.withShowAll(showAll);
        if (nameFilter != null) cmd.withImageNameFilter(nameFilter);

        LOGGER.info("Listing images" + (nameFilter != null ? " with filter \"" + nameFilter + "\"" : "") + "...");
        return cmd.exec();
    }

    /**
     * Inspect an image by its ID.
     * @param image The image to inspect, with the repository and the registry if needed.
     * @return The inspected image.
     */
    public InspectImageResponse inspectImage(@NotNull String image) {
        LOGGER.info("Inspecting image: " + image + "...");
        return this.manager.getClient().inspectImageCmd(image).exec();
    }

    /**
     * Save an image to a tar archive. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null. <br>
     * /!\ Warning: Don't forget to close the stream after usage to avoid memory leaks.
     * @param image The image to save, with the repository and the registry if needed.
     * @param tag The tag to save (optional).
     * @return The stream to the tar archive.
     */
    public InputStream saveImage(@NotNull String image, String tag) {
        final SaveImageCmd cmd = this.manager.getClient().saveImageCmd(image);

        if (tag != null) cmd.withTag(tag);

        LOGGER.info("Saving image: " + image + (tag != null ? ":" + tag : "") + "...");
        return cmd.exec();
    }

    /**
     * Save multiples images to a tar archive. <br>
     * /!\ Warning: Don't forget to close the stream after usage to avoid memory leaks.
     * @param images The images to save, with the repository and the registry if needed. The map key is the image name, the map value is the tag.
     * @return The stream to the tar archive.
     */
    public InputStream saveImages(@NotNull Map<String, String> images) {
        final SaveImagesCmd cmd = this.manager.getClient().saveImagesCmd();

        for (Map.Entry<String, String> entry : images.entrySet()) {
            cmd.withImage(entry.getKey(), entry.getValue());
        }

        LOGGER.info("Saving " + images.size() + " images...");
        return cmd.exec();
    }

    /**
     * Build an image from a Dockerfile. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param dockerfile The Dockerfile to build.
     * @param tags The tags to apply to the image (optional).
     * @return The callback.
     */
    public BuildImageCallback buildImage(@NotNull File dockerfile, String[] tags) {
        return this.buildImage(dockerfile, tags, null, null, null, null);
    }

    /**
     * Build an image from a Dockerfile. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param dockerfile The Dockerfile to build.
     * @param tags The tags to apply to the image (optional).
     * @param metadata The metadata to apply to the image (optional).
     * @param remove <code>true</code> to remove intermediate containers (optional).
     * @param platform The platform to build for (optional).
     * @param buildArgs The build arguments to apply to the image, with the key as the argument name and the value as the argument value (optional).
     * @return The callback.
     */
    public BuildImageCallback buildImage(@NotNull File dockerfile, String[] tags, Map<String, String> metadata, Boolean remove, String platform, Map<String, String> buildArgs) {
        try {
            final BuildImageCmd cmd = this.manager.getClient().buildImageCmd(dockerfile);

            if (tags != null && tags.length > 0) cmd.withTags(new HashSet<>(Arrays.asList(tags)));
            if (metadata != null) cmd.withLabels(metadata);
            if (remove != null) cmd.withRemove(remove);
            if (platform != null) cmd.withPlatform(platform);
            if (buildArgs != null) {
                for (Map.Entry<String, String> entry : buildArgs.entrySet()) {
                    cmd.withBuildArg(entry.getKey(), entry.getValue());
                }
            }

            return cmd.exec(new BuildImageCallback(dockerfile)).awaitCompletion();
        } catch (InterruptedException e) {
            LOGGER.error("An error has occurred while building the image: " + dockerfile.getName(), e);
            return null;
        }
    }

    /**
     * Tag an image. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param imageName The image to tag.
     * @param repository The repository to tag the image to.
     * @param tag The tag to tag the image to.
     * @param force <code>true</code> to force the tagging (optional).
     */
    public void tagImage(@NotNull String imageName, @NotNull String repository, @NotNull String tag, Boolean force) {
        final TagImageCmd cmd = this.manager.getClient().tagImageCmd(imageName, repository, tag);

        if (force != null) cmd.withForce(force);

        LOGGER.info("Tagging image: " + imageName + " as: " + repository + ":" + tag + "...");
        cmd.exec();
    }
}
