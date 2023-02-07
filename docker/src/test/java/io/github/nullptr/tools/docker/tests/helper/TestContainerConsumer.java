package io.github.nullptr.tools.docker.tests.helper;

import org.testcontainers.containers.GenericContainer;

@FunctionalInterface
public interface TestContainerConsumer {

    void accept(GenericContainer<?> container);
}
