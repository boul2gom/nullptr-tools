package io.github.yggdrasil80.yggtools.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.zerodep.ZerodepDockerHttpClient;
import io.github.yggdrasil80.yggtools.builder.IBuilder;
import io.github.yggdrasil80.yggtools.docker.image.DockerImageManager;
import io.github.yggdrasil80.yggtools.docker.utils.DockerHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DockerManager {

    public static final ExecutorService EXECUTORS = Executors.newCachedThreadPool();
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerManager.class);

    private final DefaultDockerClientConfig config;
    private final ZerodepDockerHttpClient httpClient;

    private final DockerImageManager imageManager;

    /**
     * The Docker manager constructor, used to manage all the docker operations through the API.
     * @param config The docker client configuration.
     * @param httpClient The docker client http client.
     */
    DockerManager(DefaultDockerClientConfig config, ZerodepDockerHttpClient httpClient) {
        this.config = config;
        this.httpClient = httpClient;

        this.imageManager = new DockerImageManager(this);
    }

    /**
     * Get the image manager instance
     * @return The image manager instance
     */
    public DockerImageManager getImageManager() {
        return this.imageManager;
    }

    /**
     * Get a new closeable docker client instance
     * @return A new closeable docker client instance
     */
    public DockerClient getClient() {
        try (final DockerClient dockerClient = DockerClientImpl.getInstance(this.config, this.httpClient)) {
            return dockerClient;
        } catch (IOException e) {
            LOGGER.error("Error while creating docker client", e);
            return null;
        }
    }

    /**
     * Get the builder instance
     * @return The builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements IBuilder<DockerManager> {

        private final DefaultDockerClientConfig.Builder config = DefaultDockerClientConfig.createDefaultConfigBuilder();
        private final ZerodepDockerHttpClient.Builder httpClient = new ZerodepDockerHttpClient.Builder();

        Builder() {}

        public Builder withHost(final DockerHost host) {
            this.config.withDockerHost(host.getHost());
            return this;
        }

        public Builder withApiVersion(final String apiVersion) {
            this.config.withApiVersion(apiVersion);
            return this;
        }

        public Builder withRegistry(final String registryUrl, final String username, final String email, final String password) {
            this.config.withRegistryUrl(registryUrl).withRegistryUsername(username).withRegistryEmail(email).withRegistryPassword(password);
            return this;
        }

        public Builder withMaxConnections(final int maxConnections) {
            this.httpClient.maxConnections(maxConnections);
            return this;
        }

        public Builder withTimeout(final int connectionTimeout, final int responseTimeout) {
            this.httpClient.connectionTimeout(Duration.ofSeconds(connectionTimeout)).responseTimeout(Duration.ofSeconds(responseTimeout));
            return this;
        }

        @Override
        public DockerManager build() {
            return new DockerManager(this.config.build(), this.httpClient.build());
        }
    }
}
