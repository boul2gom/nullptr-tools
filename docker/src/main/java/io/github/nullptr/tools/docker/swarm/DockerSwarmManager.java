package io.github.nullptr.tools.docker.swarm;

import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import io.github.nullptr.tools.docker.DockerManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manager for Docker Swarm.
 */
public class DockerSwarmManager {

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerSwarmManager.class);

    /**
     * The Docker manager.
     */
    private final DockerManager manager;

    /**
     * The docker swarm manager constructor.
     * @param manager The docker manager.
     */
    public DockerSwarmManager(DockerManager manager) {
        this.manager = manager;
    }

    /**
     * Initialize a swarm. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param swarmSpec The swarm specification.
     * @param listenAddress The listen address (optional).
     * @param advertiseAddress The advertise address (optional).
     * @param forceNewCluster Force a new cluster (optional).
     */
    public void initSwarm(@NotNull SwarmSpec swarmSpec, String listenAddress, String advertiseAddress, Boolean forceNewCluster) {
        final InitializeSwarmCmd cmd = this.manager.getClient().initializeSwarmCmd(swarmSpec);

        if (listenAddress != null) cmd.withListenAddr(listenAddress);
        if (advertiseAddress != null) cmd.withAdvertiseAddr(advertiseAddress);
        if (forceNewCluster != null) cmd.withForceNewCluster(forceNewCluster);

        LOGGER.info("Initializing a new swarm " + swarmSpec.getName() + "...");
        cmd.exec();
    }

    /**
     * Inspect the running swarm.
     * @return The swarm.
     */
    public Swarm inspectSwarm() {
        LOGGER.info("Inspecting the swarm...");
        return this.manager.getClient().inspectSwarmCmd().exec();
    }

    /**
     * Join a swarm. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param remoteAddress The remote address (optional).
     * @param joinToken The join token (optional).
     * @param listenAddress The listen address (optional).
     * @param advertiseAddress The advertise address (optional).
     */
    public void joinSwarm(String remoteAddress, String joinToken, String listenAddress, String advertiseAddress) {
        final JoinSwarmCmd cmd = this.manager.getClient().joinSwarmCmd();

        if (remoteAddress != null) cmd.withRemoteAddrs(Collections.singletonList(remoteAddress));
        if (joinToken != null) cmd.withJoinToken(joinToken);
        if (listenAddress != null) cmd.withListenAddr(listenAddress);
        if (advertiseAddress != null) cmd.withAdvertiseAddr(advertiseAddress);

        LOGGER.info("Joining swarm...");
        cmd.exec();
    }

    /**
     * Leave swarm. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param force <code>true</code> to force the leave, <code>false</code> otherwise (optional).
     */
    public void leaveSwarm(Boolean force) {
        final LeaveSwarmCmd cmd = this.manager.getClient().leaveSwarmCmd();

        if (force != null) cmd.withForceEnabled(force);

        LOGGER.info("Leaving swarm...");
        cmd.exec();
    }

    /**
     * Update swarm. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param swarmSpec The swarm specification.
     * @param version The version (optional).
     * @param rotateWorkerToken <code>true</code> to rotate the worker token, <code>false</code> otherwise (optional).
     * @param rotateManagerToken <code>true</code> to rotate the manager token, <code>false</code> otherwise (optional).
     */
    public void updateSwarm(@NotNull SwarmSpec swarmSpec, Long version, Boolean rotateWorkerToken, Boolean rotateManagerToken) {
        final UpdateSwarmCmd cmd = this.manager.getClient().updateSwarmCmd(swarmSpec);

        if (version != null) cmd.withVersion(version);
        if (rotateWorkerToken != null) cmd.withRotateWorkerToken(rotateWorkerToken);
        if (rotateManagerToken != null) cmd.withRotateManagerToken(rotateManagerToken);

        LOGGER.info("Updating swarm...");
        cmd.exec();
    }

    /**
     * Update a swarm node. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param swarmNodeId The swarm node ID.
     * @param swarmNodeSpec The swarm node specification (optional).
     * @param version The version (optional).
     */
    public void updateNode(@NotNull String swarmNodeId, SwarmNodeSpec swarmNodeSpec, Long version) {
        final UpdateSwarmNodeCmd cmd = this.manager.getClient().updateSwarmNodeCmd().withSwarmNodeId(swarmNodeId);

        if (swarmNodeSpec != null) cmd.withSwarmNodeSpec(swarmNodeSpec);
        if (version != null) cmd.withVersion(version);

        LOGGER.info("Updating swarm node " + swarmNodeId + "...");
        cmd.exec();
    }

    /**
     * Remove a swarm node. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param swarmNodeId The swarm node ID.
     * @param force <code>true</code> to force the remove, <code>false</code> otherwise (optional).
     */
    public void removeNode(@NotNull String swarmNodeId, Boolean force) {
        final RemoveSwarmNodeCmd cmd = this.manager.getClient().removeSwarmNodeCmd(swarmNodeId);

        if (force != null) cmd.withForce(force);

        LOGGER.info("Removing swarm node " + swarmNodeId + "...");
        cmd.exec();
    }

    /**
     * List the swarm nodes, filtered by the given filters if any. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param id The node ID (optional).
     * @param name The node name (optional).
     * @param membership The node membership (optional).
     * @param role The node role (optional).
     * @return The list of swarm nodes.
     */
    public List<SwarmNode> listNodes(String id, String name, String membership, String role) {
        final ListSwarmNodesCmd cmd = this.manager.getClient().listSwarmNodesCmd();

        if (id != null) cmd.withIdFilter(Collections.singletonList(id));
        if (name != null) cmd.withNameFilter(Collections.singletonList(name));
        if (membership != null) cmd.withMembershipFilter(Collections.singletonList(membership));
        if (role != null) cmd.withRoleFilter(Collections.singletonList(role));

        LOGGER.info("Listing swarm nodes...");
        return cmd.exec();
    }

    /**
     * List the running services, filtered by the given filters if any. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param id The service ID (optional).
     * @param name The service name (optional).
     * @return The list of running services.
     */
    public List<Service> listServices(String id, String name) {
        final ListServicesCmd cmd = this.manager.getClient().listServicesCmd();

        if (id != null) cmd.withIdFilter(Collections.singletonList(id));
        if (name != null) cmd.withNameFilter(Collections.singletonList(name));

        LOGGER.info("Listing services...");
        return cmd.exec();
    }

    /**
     * Create a new service. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param serviceSpec The service specification.
     * @param authConfig The authentication configuration (optional).
     * @return The created service.
     */
    public CreateServiceResponse createService(@NotNull ServiceSpec serviceSpec, AuthConfig authConfig) {
        final CreateServiceCmd cmd = this.manager.getClient().createServiceCmd(serviceSpec);

        if (authConfig != null) cmd.withAuthConfig(authConfig);

        LOGGER.info("Creating service " + serviceSpec.getName() + "...");
        return cmd.exec();
    }

    /**
     * Inspect a service.
     * @param serviceId The service ID.
     * @return The service.
     */
    public Service inspectService(@NotNull String serviceId) {
        LOGGER.info("Inspecting service " + serviceId + "...");
        return this.manager.getClient().inspectServiceCmd(serviceId).exec();
    }

    /**
     * Update a service. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param serviceId The service ID.
     * @param serviceSpec The service specification.
     * @param version The service version (optional).
     */
    public void updateService(@NotNull String serviceId, @NotNull ServiceSpec serviceSpec, Long version) {
        final UpdateServiceCmd cmd = this.manager.getClient().updateServiceCmd(serviceId, serviceSpec);

        if (version != null) cmd.withVersion(version);

        LOGGER.info("Updating service " + serviceId + "...");
        cmd.exec();
    }

    /**
     * Remove a service.
     * @param serviceId The service ID.
     */
    public void removeService(@NotNull String serviceId) {
        LOGGER.info("Removing service " + serviceId + "...");
        this.manager.getClient().removeServiceCmd(serviceId).exec();
    }

    /**
     * List the tasks, filtered by the given filters if any. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param id The task ID (optional).
     * @param name The task name (optional).
     * @param service The task service (optional).
     * @param node The task node (optional).
     * @param state The task state (optional).
     * @return The list of tasks.
     */
    public List<Task> listTasks(String id, String name, String service, String node, TaskState state) {
        final ListTasksCmd cmd = this.manager.getClient().listTasksCmd();

        if (id != null) cmd.withIdFilter(id);
        if (name != null) cmd.withNameFilter(name);
        if (service != null) cmd.withServiceFilter(service);
        if (node != null) cmd.withNodeFilter(node);
        if (state != null) cmd.withStateFilter(state);

        LOGGER.info("Listing tasks...");
        return cmd.exec();
    }

    /**
     * List secrets, filtered by the given filters if any. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param id The secret ID (optional).
     * @param name The secret name (optional).
     * @return The list of secrets.
     */
    public List<Secret> listSecrets(String id, String name) {
        final ListSecretsCmd cmd = this.manager.getClient().listSecretsCmd();

        if (id != null) cmd.withIdFilter(Collections.singletonList(id));
        if (name != null) cmd.withNameFilter(Collections.singletonList(name));

        LOGGER.info("Listing secrets...");
        return cmd.exec();
    }

    /**
     * Create a new secret.
     * @param secretSpec The secret specification.
     * @return The created secret.
     */
    public CreateSecretResponse createSecret(@NotNull SecretSpec secretSpec) {
        LOGGER.info("Creating secret " + secretSpec.getName() + "...");
        return this.manager.getClient().createSecretCmd(secretSpec).exec();
    }

    /**
     * Remove a secret.
     * @param secretId The secret ID.
     */
    public void removeSecret(@NotNull String secretId) {
        LOGGER.info("Removing secret " + secretId + "...");
        this.manager.getClient().removeSecretCmd(secretId).exec();
    }

    /**
     * List configs, filtered by the given filters if any. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param id The config ID (optional).
     * @param name The config name (optional).
     * @return The list of configs.
     */
    public List<Config> listConfigs(String id, String name) {
        final ListConfigsCmd cmd = this.manager.getClient().listConfigsCmd();
        final Map<String, List<String>> filters = new HashMap<>();

        if (id != null) filters.put("id", Collections.singletonList(id));
        if (name != null) filters.put("name", Collections.singletonList(name));

        if (!filters.isEmpty()) cmd.withFilters(filters);

        LOGGER.info("Listing configs...");
        return cmd.exec();
    }

    /**
     * Create a new config. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param name The config name (optional).
     * @param data The config data (optional).
     * @return The created config.
     */
    public CreateConfigResponse createConfig(String name, byte[] data) {
        final CreateConfigCmd cmd = this.manager.getClient().createConfigCmd();

        if (name != null) cmd.withName(name);
        if (data != null) cmd.withData(data);

        LOGGER.info("Creating config" + (name != null ? " " + name : "") + "...");
        return cmd.exec();
    }

    /**
     * Inspect a config.
     * @param id The config ID.
     * @return The config.
     */
    public Config inspectConfig(@NotNull String id) {
        LOGGER.info("Inspecting config " + id + "...");
        return this.manager.getClient().inspectConfigCmd(id).exec();
    }

    /**
     * Remove a config.
     * @param id The config ID.
     */
    public void removeConfig(@NotNull String id) {
        LOGGER.info("Removing config " + id + "...");
        this.manager.getClient().removeConfigCmd(id).exec();
    }
}
