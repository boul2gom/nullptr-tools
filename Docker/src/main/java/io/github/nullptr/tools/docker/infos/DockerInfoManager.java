package io.github.nullptr.tools.docker.infos;

import com.github.dockerjava.api.command.AuthCmd;
import com.github.dockerjava.api.command.PruneCmd;
import com.github.dockerjava.api.model.*;
import io.github.nullptr.tools.docker.DockerManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manager for docker infos.
 */
public class DockerInfoManager {

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerInfoManager.class);

    /**
     * The docker manager.
     */
    private final DockerManager manager;

    /**
     * The docker info manager constructor.
     * @param manager The docker manager.
     */
    public DockerInfoManager(DockerManager manager) {
        this.manager = manager;
    }

    /**
     * Authenticate to a registry. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param authConfig The auth config (optional).
     * @return The auth response.
     */
    public AuthResponse authToRegistry(AuthConfig authConfig) {
        final AuthCmd cmd = this.manager.getClient().authCmd();

        if (authConfig != null) cmd.withAuthConfig(authConfig);

        LOGGER.info("Authenticating to registry...");
        return cmd.exec();
    }

    /**
     * Get the docker info.
     * @return The docker info.
     */
    public Info getInfos() {
        LOGGER.info("Getting docker infos...");
        return this.manager.getClient().infoCmd().exec();
    }

    /**
     * Ping the docker daemon.
     */
    public void pingServer() {
        LOGGER.info("Pinging docker daemon...");
        this.manager.getClient().pingCmd().exec();
    }

    /**
     * Get the docker version.
     * @return The docker version.
     */
    public Version getVersion() {
        LOGGER.info("Getting docker version...");
        return this.manager.getClient().versionCmd().exec();
    }

    /**
     * Prune the docker system. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param type The type of prune.
     * @param until The date until which the prune will be done (optional).
     * @param imageDangling Whether to prune dangling images, only works if the given type is "IMAGES" (optional).
     * @return The prune response.
     */
    public PruneResponse makePrune(@NotNull PruneType type, String until, Boolean imageDangling) {
        final PruneCmd cmd = this.manager.getClient().pruneCmd(type);

        if (until != null) cmd.withUntilFilter(until);
        if (imageDangling != null) cmd.withDangling(imageDangling);

        LOGGER.info("Making prune...");
        return cmd.exec();
    }
}
