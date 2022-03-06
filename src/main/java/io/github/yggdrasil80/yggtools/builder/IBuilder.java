package io.github.yggdrasil80.yggtools.builder;

/**
 * Interface to create a builder.
 * @param <T> The object built by the builder.
 */
public interface IBuilder<T> {

    /**
     * Builds the object.
     * @return The object built.
     */
    T build();
}
