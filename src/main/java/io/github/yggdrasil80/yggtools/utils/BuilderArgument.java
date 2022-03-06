package io.github.yggdrasil80.yggtools.utils;

import java.util.function.Supplier;

/**
 * Represents a builder argument.
 * @param <T> The type of the argument.
 */
public final class BuilderArgument<T> {

    private T object;
    private final String objectName;
    private final boolean isRequired;

    /**
     * The BuilderArgument constructor.
     * @param objectName The name of the argument object.
     * @param object The argument object.
     * @param isRequired <code>true</code> if the argument is required, <code>false</code> otherwise.
     */
    public BuilderArgument(String objectName, Supplier<T> object, boolean isRequired) {
        this.objectName = objectName;
        this.object = object.get();
        this.isRequired = isRequired;
    }

    /**
     * Returns the argument object.
     * @return The argument object.
     */
    public T get() {
        if (this.isRequired) {
            if (this.object != null) {
                return this.object;
            } else throw new RuntimeException("Object " + this.objectName + " is null !");
        } else return this.object;
    }

    /**
     * Sets the argument object.
     * @param object The argument object to set.
     */
    public void set(T object) {
        this.object = object;
    }
}
