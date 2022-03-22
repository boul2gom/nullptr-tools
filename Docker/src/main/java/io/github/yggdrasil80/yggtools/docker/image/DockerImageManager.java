package io.github.yggdrasil80.yggtools.docker.image;

import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.SearchItem;
import io.github.yggdrasil80.yggtools.docker.DockerManager;
import io.github.yggdrasil80.yggtools.docker.utils.DockerCallbackCreator;
import io.github.yggdrasil80.yggtools.types.Pair;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;

public class DockerImageManager extends DockerCallbackCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerImageManager.class);

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
     * @param platform The platform of the image to pull (optional).
     * @param registry The registry to pull the image from (optional).
     * @param auth The authentication to use (optional).
     */
    public void pullImage(@NotNull String image, String tag, String platform, String registry, AuthConfig auth) {
        try {
            final PullImageCmd cmd = this.manager.getClient().pullImageCmd(image);
            final PullImageResultCallback callback = new PullImageResultCallback() {
                @Override
                public void onStart(Closeable stream) {
                    super.onStart(stream);

                    LOGGER.info("Pulling image: " + image + (tag != null ? ":" + tag : "") + "...");
                }

                @Override
                public void onError(Throwable throwable) {
                    super.onError(throwable);

                    LOGGER.error("Failed to pull image: " + image + (tag != null ? ":" + tag : "") + ".", throwable);
                }

                @Override
                public void onComplete() {
                    super.onComplete();

                    LOGGER.info("Successfully pulled image: " + image + (tag != null ? ":" + tag : "") + ".");
                }
            };

            if (tag != null) cmd.withTag(tag);
            if (platform != null) cmd.withPlatform(platform);
            if (registry != null) cmd.withRegistry(registry);
            if (auth != null) cmd.withAuthConfig(auth); else cmd.withAuthConfig(this.manager.getClient().authConfig());

            cmd.exec(callback).awaitCompletion();
        } catch (InterruptedException e) {
            LOGGER.error("An error has occurred when pulling image: " + image + (tag != null ? ":" + tag : "") + ".", e);
        }
    }

    /**
     * Push an image to a registry. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param image The image to push, with the repository and the registry if needed. <br>
     * Example: "ghcr.io/yggdrasil80/my-image" for custom registry, "yggdrasil80/my-image" or "my-image" if the registry is Docker Hub.
     * @param tag The tag of the image to push (optional).
     * @param auth The authentication to use (optional).
     */
    public void pushImage(@NotNull String image, String tag, AuthConfig auth) {
        final PushImageCmd cmd = this.manager.getClient().pushImageCmd(image);

        if (tag != null) cmd.withTag(tag);
        if (auth != null) cmd.withAuthConfig(auth); else cmd.withAuthConfig(this.manager.getClient().authConfig());

        cmd.exec(DockerCallbackCreator.create(LOGGER,
                "Pushing image: " + image + (tag != null ? ":" + tag : "") + "...",
                "Failed to push image: " + image + (tag != null ? ":" + tag : "") + ".",
                "Successfully pushed image: " + image + (tag != null ? ":" + tag : "") + "."
        ));
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
     * @param images The images to save, with the repository and the registry if needed. The left part of the pair is the image, the right part is the tag.
     * @return The stream to the tar archive.
     */
    public InputStream saveImages(@NotNull List<Pair<String, String>> images) {
        final SaveImagesCmd cmd = this.manager.getClient().saveImagesCmd();

        for (Pair<String, String> image : images) {
            cmd.withImage(image.getLeft(), image.getRight());
        }

        LOGGER.info("Saving " + images.size() + " images...");
        return cmd.exec();
    }

    /**
     * Build an image from a Dockerfile. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param dockerfile The Dockerfile to build.
     * @param tags The tags to apply to the image (optional).
     * @param metadata The metadata to apply to the image (optional).
     * @param remove <code>true</code> to remove intermediate containers (optional).
     * @param platform The platform to build for (optional).
     * @param buildArgs The build arguments to apply to the image, with the left part of the pair as the argument name and the right part as the argument value (optional).
     */
    public void buildImage(@NotNull File dockerfile, String[] tags, List<Pair<String, String>> metadata, Boolean remove, String platform, List<Pair<String, String>> buildArgs) {
        final BuildImageCmd cmd = this.manager.getClient().buildImageCmd(dockerfile);

        if (tags != null && tags.length > 0) cmd.withTags(new HashSet<>(Arrays.asList(tags)));
        if (metadata != null && !metadata.isEmpty()) {
            final Map<String, String> labels = new HashMap<>();
            for (Pair<String, String> label : metadata) {
                labels.put(label.getLeft(), label.getRight());
            }
            cmd.withLabels(labels);
        }
        if (remove != null) cmd.withRemove(remove);
        if (platform != null) cmd.withPlatform(platform);
        if (buildArgs != null && !buildArgs.isEmpty()) {
            for (Pair<String, String> buildArg : buildArgs) {
                cmd.withBuildArg(buildArg.getLeft(), buildArg.getRight());
            }
        }

        cmd.exec(DockerCallbackCreator.create(LOGGER,
                "Building image from: " + dockerfile.getAbsolutePath() + "...",
                "An error has occurred when building image from: " + dockerfile.getAbsolutePath() + ".",
                "Successfully built image from: " + dockerfile.getAbsolutePath() + ".")
        );
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
