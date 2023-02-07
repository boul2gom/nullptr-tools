package io.github.nullptr.tools.docker.utils;

import io.github.nullptr.tools.platform.PlatformHelper;
import io.github.nullptr.tools.platform.PlatformOS;
import org.slf4j.LoggerFactory;

/**
 * Enum for the different docker hosts.
 */
public enum DockerHost {

    /**
     * Docker host for the local unix machine.
     */
    UNIX_SOCKET("unix:///var/run/docker.sock"),
    /**
     * Docker host for the local Windows machine, encrypted.
     */
    TCP_DAEMON_ENCRYPTED("tcp://localhost:2376"),
    /**
     * Docker host for the local Windows machine, unencrypted.
     */
    TCP_DAEMON("tcp://localhost:2375");

    /**
     * The host.
     */
    private final String host;

    /**
     * The DockerHost constructor.
     * @param host The host.
     */
    DockerHost(String host) {
        this.host = host;
    }

    /**
     * Get the host, with a little platform checking to warn about the compatibility.
     * @return The host.
     */
    public String getHost() {
        if (PlatformHelper.isOn(PlatformOS.WINDOWS) && this.host.startsWith("unix://")) {
            LoggerFactory.getLogger(DockerHost.class).warn("Windows does not support unix sockets. Use tcp instead.");
        }

        return this.host;
    }
}
