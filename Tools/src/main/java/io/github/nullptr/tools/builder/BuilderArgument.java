package io.github.nullptr.tools.builder;

import java.util.function.Supplier;

/**
 * Represents an argument for the builder.
 * @param <T> The type of the argument.
 */
public class BuilderArgument<T> {

    /**
     * The name of the object.
     */
    private final String objectName;
    /**
     * The supplier of the object, to get when building.
     */
    private Supplier<T> object = null;
    /**
     * <code>true</code> if the object is required.
     */
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
     * Get the argument object.
     * @return The argument object.
     */
    public T get() {
        if(this.isRequired && this.object == null) {
            throw new IllegalArgumentException("The argument " + this.objectName + " is required.");
        }
        return this.object.get();
    }

    /**
     * Set the argument object.
     * @param object The argument object to set.
     */
    public void set(Supplier<T> object) {
        this.object = object;
    }

    /**
     * Set argument as not required
     * @return The builder argument.
     */
    public BuilderArgument<T> optional() {
        this.isRequired = false;
        return this;
    }

    /**
     * Set argument as required
     * @return The builder argument.
     */
    public BuilderArgument<T> required() {
        this.isRequired = true;
        return this;
    }
}
