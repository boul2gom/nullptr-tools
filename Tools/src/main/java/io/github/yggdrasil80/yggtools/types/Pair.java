package io.github.yggdrasil80.yggtools.types;

public class Pair<Left, Right> {

    private final Left left;
    private final Right right;

    public Pair(final Left left, final Right right) {
        this.left = left;
        this.right = right;
    }

    public Left getLeft() {
        return this.left;
    }

    public Right getRight() {
        return this.right;
    }
}
