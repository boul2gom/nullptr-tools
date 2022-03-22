package io.github.yggdrasil80.yggtools.docker.utils;

import org.slf4j.LoggerFactory;

/**
 * Enum for the different docker hosts.
 */
public enum DockerHost {

    UNIX_SOCKET("unix:///var/run/docker.sock"),
    TCP_DAEMON_ENCRYPTED("tcp://localhost:2376"),
    TCP_DAEMON("tcp://localhost:2375");

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
        if (System.getProperty("os.name").toLowerCase().contains("win") && this.host.startsWith("unix://")) {
            LoggerFactory.getLogger(DockerHost.class).warn("Windows does not support unix sockets. Using tcp instead.");
        }

        return this.host;
    }
}
