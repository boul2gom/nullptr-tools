package io.github.nullptr.tools.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.zerodep.ZerodepDockerHttpClient;
import io.github.nullptr.tools.builder.IBuilder;
import io.github.nullptr.tools.docker.container.DockerContainerManager;
import io.github.nullptr.tools.docker.image.DockerImageManager;
import io.github.nullptr.tools.docker.infos.DockerInfoManager;
import io.github.nullptr.tools.docker.network.DockerNetworkManager;
import io.github.nullptr.tools.docker.swarm.DockerSwarmManager;
import io.github.nullptr.tools.docker.utils.DockerHost;
import io.github.nullptr.tools.docker.volume.DockerVolumeManager;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

/**
 * Manager for docker operations.
 */
public class DockerManager {

    /**
     * The docker client.
     */
    private final DockerClient dockerClient;
    /**
     * The info manager.
     */
    private final DockerInfoManager infoManager;
    /**
     * The image manager.
     */
    private final DockerImageManager imageManager;
    /**
     * The swarm manager.
     */
    private final DockerSwarmManager swarmManager;
    /**
     * The volume manager.
     */
    private final DockerVolumeManager volumeManager;
    /**
     * The network manager.
     */
    private final DockerNetworkManager networkManager;
    /**
     * The container manager.
     */
    private final DockerContainerManager containerManager;

    /**
     * The Docker manager constructor, used to manage all the docker operations through the API.
     * @param config The docker client configuration.
     * @param httpClient The docker client http client.
     */
    DockerManager(DefaultDockerClientConfig config, ZerodepDockerHttpClient httpClient) {
        try {
            this.dockerClient = DockerClientImpl.getInstance(config, httpClient);
        } catch (Exception e) {
            LoggerFactory.getLogger(DockerManager.class).error("Error while creating docker client", e);
            throw new RuntimeException(e);
        }

        this.infoManager = new DockerInfoManager(this);
        this.imageManager = new DockerImageManager(this);
        this.swarmManager = new DockerSwarmManager(this);
        this.volumeManager = new DockerVolumeManager(this);
        this.networkManager = new DockerNetworkManager(this);
        this.containerManager = new DockerContainerManager(this);
    }

    /**
     * Get the docker client instance.
     * @return The docker client instance.
     */
    public DockerClient getClient() {
        return this.dockerClient;
    }

    /**
     * Get the info manager.
     * @return The info manager.
     */
    public DockerInfoManager getInfoManager() {
        return this.infoManager;
    }

    /**
     * Get the image manager.
     * @return The image manager.
     */
    public DockerImageManager getImageManager() {
        return this.imageManager;
    }

    /**
     * Get the swarm manager.
     * @return The swarm manager.
     */
    public DockerSwarmManager getSwarmManager() {
        return this.swarmManager;
    }

    /**
     * Get the volume manager.
     * @return The volume manager.
     */
    public DockerVolumeManager getVolumeManager() {
        return this.volumeManager;
    }

    /**
     * Get the network manager.
     * @return The network manager.
     */
    public DockerNetworkManager getNetworkManager() {
        return this.networkManager;
    }

    /**
     * Get the container manager.
     * @return The container manager.
     */
    public DockerContainerManager getContainerManager() {
        return this.containerManager;
    }

    /**
     * The builder for the docker manager.
     */
    public static class Builder implements IBuilder<DockerManager> {

        /**
         * The docker client configuration.
         */
        private final DefaultDockerClientConfig.Builder config = DefaultDockerClientConfig.createDefaultConfigBuilder();
        /**
         * The docker client http client configuration.
         */
        private final ZerodepDockerHttpClient.Builder httpClient = new ZerodepDockerHttpClient.Builder();

        /**
         * Set the docker host.
         * @param host The docker host.
         * @return The builder instance.
         */
        public Builder withHost(DockerHost host) {
            return this.withHost(host.getHost());
        }

        /**
         * Set the docker host.
         * @param host The docker host.
         * @return The builder instance.
         */
        public Builder withHost(String host) {
            try {
                this.config.withDockerHost(host);
                this.httpClient.dockerHost(new URI(host));
            } catch (URISyntaxException e) {
                LoggerFactory.getLogger(DockerManager.class).error("Error while building docker client, invalid host", e);
            }

            return this;
        }

        /**
         * Set the API version.
         * @param apiVersion The API version.
         * @return The builder instance.
         */
        public Builder withApiVersion(String apiVersion) {
            this.config.withApiVersion(apiVersion);
            return this;
        }

        /**
         * Set the docker registry.
         * @param registryUrl The registry url.
         * @param username The account username.
         * @param email The account email.
         * @param password The account password.
         * @return The builder instance.
         */
        public Builder withRegistry(String registryUrl, String username, String email, String password) {
            this.config.withRegistryUrl(registryUrl).withRegistryUsername(username).withRegistryEmail(email).withRegistryPassword(password);
            return this;
        }

        /**
         * Set the max connections.
         * @param maxConnections The max connections.
         * @return The builder instance.
         */
        public Builder withMaxConnections(int maxConnections) {
            this.httpClient.maxConnections(maxConnections);
            return this;
        }

        /**
         * Set the connection timeout.
         * @param connectionTimeout The connection timeout, in seconds.
         * @param responseTimeout The response timeout, in seconds.
         * @return The builder instance.
         */
        public Builder withTimeout(int connectionTimeout, int responseTimeout) {
            this.httpClient.connectionTimeout(Duration.ofSeconds(connectionTimeout)).responseTimeout(Duration.ofSeconds(responseTimeout));
            return this;
        }

        /**
         * Build the docker manager.
         * @return The built docker manager.
         */
        @Override
        public DockerManager build() {
            return new DockerManager(this.config.build(), this.httpClient.build());
        }
    }
}
