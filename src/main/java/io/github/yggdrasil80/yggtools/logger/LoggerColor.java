package io.github.yggdrasil80.yggtools.logger;

public enum LoggerColor {

    RESET("\u001B[0m"),
    RED("\u001B[31m"),
    YELLOW("\u001B[33m"),
    CYAN("\u001B[36m");

    private final String color;

    LoggerColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return this.color;
    }
}
