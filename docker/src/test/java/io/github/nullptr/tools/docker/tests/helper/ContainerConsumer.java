package io.github.nullptr.tools.docker.tests.helper;

import com.github.dockerjava.api.command.CreateContainerResponse;

@FunctionalInterface
public interface ContainerConsumer {

    void accept(CreateContainerResponse container);
}
