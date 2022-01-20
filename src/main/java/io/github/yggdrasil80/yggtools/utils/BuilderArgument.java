package io.github.yggdrasil80.yggtools.utils;

import java.util.function.Supplier;

public class BuilderArgument<T> {

    private T object;
    private final String objectName;
    private final boolean isRequired;

    public BuilderArgument(String objectName, Supplier<T> object, boolean isRequired) {
        this.objectName = objectName;
        this.object = object.get();
        this.isRequired = isRequired;
    }

    public T get() {
        if (this.isRequired) {
            if (this.object != null) return this.object;
            else throw new RuntimeException("Object " + this.objectName + " is null !");
        } else return this.object;
    }

    public void set(T object) {
        this.object = object;
    }
}
