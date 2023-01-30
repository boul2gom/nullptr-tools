package io.github.nullptr.tools.docker.network;

import com.github.dockerjava.api.command.CreateNetworkCmd;
import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.command.DisconnectFromNetworkCmd;
import com.github.dockerjava.api.command.ListNetworksCmd;
import com.github.dockerjava.api.model.Network;
import io.github.nullptr.tools.docker.DockerManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Manager for Docker networks.
 */
public class DockerNetworkManager {

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerNetworkManager.class);

    /**
     * The Docker manager.
     */
    private final DockerManager manager;

    /**
     * The docker network manager constructor.
     * @param manager The docker manager.
     */
    public DockerNetworkManager(DockerManager manager) {
        this.manager = manager;
    }

    /**
     * Lists all networks, filtered by the given filters if any. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param name The name of the network.
     * @param id The id of the network.
     * @return The list of networks, filtered by the given filters if any.
     */
    public List<Network> listNetworks(String name, String id) {
        final ListNetworksCmd cmd = this.manager.getClient().listNetworksCmd();

        if (name != null) cmd.withNameFilter(name);
        if (id != null) cmd.withIdFilter(id);

        LOGGER.info("Listing networks...");
        return cmd.exec();
    }

    /**
     * Inspects a network.
     * @param id The id of the network.
     * @return The network.
     */
    public Network inspectNetwork(@NotNull String id) {
        LOGGER.info("Inspecting network " + id + "...");
        return this.manager.getClient().inspectNetworkCmd().withNetworkId(id).exec();
    }

    /**
     * Creates a network.
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param name The name of the network.
     * @param driver The driver of the network (optional).
     * @return The created network.
     */
    public CreateNetworkResponse creatNetwork(@NotNull String name, String driver) {
        return this.createNetwork(name, driver, null, null, null, null, null, true);
    }

    /**
     * Creates a network. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param name The name of the network.
     * @param driver The driver of the network (optional).
     * @param ipam The ipam of the network (optional).
     * @param options The options of the network (optional).
     * @param checkDuplicate <code>true</code> if you want to check if the network already exists, <code>false</code> otherwise (optional).
     * @param internal <code>true</code> if you want to restrict external access to the network, <code>false</code> otherwise (optional).
     * @param enableIPv6 <code>true</code> if you want to enable IPv6, <code>false</code> otherwise (optional).
     * @param attachable <code>true</code> if you want to attach the network to containers, <code>false</code> otherwise (optional).
     * @return The created network.
     */
    public CreateNetworkResponse createNetwork(@NotNull String name, String driver, Network.Ipam ipam, Map<String, String> options,
           Boolean checkDuplicate, Boolean internal, Boolean enableIPv6, Boolean attachable) {
        final CreateNetworkCmd cmd = this.manager.getClient().createNetworkCmd().withName(name);

        if (driver != null) cmd.withDriver(driver);
        if (ipam != null) cmd.withIpam(ipam);
        if (options != null) cmd.withOptions(options);
        if (checkDuplicate != null) cmd.withCheckDuplicate(checkDuplicate);
        if (internal != null) cmd.withInternal(internal);
        if (enableIPv6 != null) cmd.withEnableIpv6(enableIPv6);
        if (attachable != null) cmd.withAttachable(attachable);

        LOGGER.info("Creating network " + name + "...");
        return cmd.exec();
    }

    /**
     * Removes a network.
     * @param id The id of the network.
     */
    public void removeNetwork(@NotNull String id) {
        LOGGER.info("Removing network " + id + "...");
        this.manager.getClient().removeNetworkCmd(id).exec();
    }

    /**
     * Connects a container to a network.
     * @param networkId The id of the network.
     * @param containerId The id of the container.
     */
    public void connectToNetwork(@NotNull String networkId, @NotNull String containerId) {
        LOGGER.info("Connecting container " + containerId + " to network " + networkId + "...");
        this.manager.getClient().connectToNetworkCmd().withNetworkId(networkId).withContainerId(containerId).exec();
    }

    /**
     * Disconnects a container from a network. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param networkId The id of the network.
     * @param containerId The id of the container.
     * @param force <code>true</code> if you want to force the disconnection, <code>false</code> otherwise (optional).
     */
    public void disconnectFromNetwork(@NotNull String networkId, @NotNull String containerId, Boolean force) {
        final DisconnectFromNetworkCmd cmd = this.manager.getClient().disconnectFromNetworkCmd().withNetworkId(networkId).withContainerId(containerId);

        if (force != null) cmd.withForce(force);

        LOGGER.info("Disconnecting container " + containerId + " from network " + networkId + "...");
        cmd.exec();
    }
}
