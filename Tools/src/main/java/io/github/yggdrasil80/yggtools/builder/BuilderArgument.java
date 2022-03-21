package io.github.yggdrasil80.yggtools.builder;

import java.util.function.Supplier;

/**
 * Represents a builder argument.
 * @param <T> The type of the argument.
 */
public class BuilderArgument<T> {

    private final String objectName;
    private Supplier<T> object = null;
    private boolean isRequired = false;

    /**
     * The BuilderArgument constructor.
     * @param objectName The name of the object.
     */
    public BuilderArgument(String objectName) {
        this.objectName = objectName;
    }

    /**
     * The BuilderArgument constructor.
     * @param objectName The name of the argument object.
     * @param object The argument object.
     */
    public BuilderArgument(String objectName, Supplier<T> object) {
        this.objectName = objectName;
        this.object = object;
    }

    /**
     * Returns the argument object.
     * @return The argument object.
     */
    public T get() {
        if(this.isRequired && this.object == null) {
            throw new IllegalArgumentException("The argument " + this.objectName + " is required.");
        }
        return this.object.get();
    }

    /**
     * Sets the argument object.
     * @param object The argument object to set.
     */
    public void set(Supplier<T> object) {
        this.object = object;
    }

    /**
     * Set argument not required
     * @return {@link BuilderArgument}
     */
    public BuilderArgument<T> optional() {
        this.isRequired = false;
        return this;
    }

    /**
     * Set argument required
     * @return {@link BuilderArgument}
     */
    public BuilderArgument<T> required() {
        this.isRequired = true;
        return this;
    }
}
