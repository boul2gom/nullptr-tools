package io.github.yggdrasil80.yggtools.types;

/**
 * A pair of objects.
 * @param <Left> The type of the left object.
 * @param <Right> The type of the right object.
 */
public class Pair<Left, Right> {

    /**
     * The left object.
     */
    private final Left left;
    /**
     * The right object.
     */
    private final Right right;

    /**
     * Constructs a new pair.
     * @param left The left object.
     * @param right The right object.
     */
    public Pair(Left left, Right right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Get the left object.
     * @return The left object.
     */
    public Left getLeft() {
        return this.left;
    }

    /**
     * Get the right object.
     * @return The right object.
     */
    public Right getRight() {
        return this.right;
    }
}
