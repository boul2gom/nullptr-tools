package io.github.yggdrasil80.yggtools.docker.dockerfile;

/**
 * Dockerfile's instruction.
 */
public enum DockerInstruction {

    /**
     * FROM instruction.
     */
    FROM,
    /**
     * MAINTAINER instruction.
     */
    MAINTAINER,
    /**
     * RUN instruction.
     */
    RUN,
    /**
     * CMD instruction.
     */
    CMD,
    /**
     * LABEL instruction.
     */
    LABEL,
    /**
     * ENV instruction.
     */
    ENV,
    /**
     * EXPOSE instruction.
     */
    EXPOSE,
    /**
     * ADD instruction.
     */
    ADD,
    /**
     * COPY instruction.
     */
    COPY,
    /**
     * ENTRYPOINT instruction.
     */
    ENTRYPOINT,
    /**
     * VOLUME instruction.
     */
    VOLUME,
    /**
     * USER instruction.
     */
    USER,
    /**
     * WORKDIR instruction.
     */
    WORKDIR,
    /**
     * ARG instruction.
     */
    ARG
}
