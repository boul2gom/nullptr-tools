package io.github.nullptr.tools.logger;

/**
 * Enum for the colors of the logger.
 */
public enum LoggerColor {

    /**
     * White color.
     */
    RESET("\u001B[0m"),
    /**
     * Red color.
     */
    RED("\u001B[31m"),
    /**
     * Yellow color.
     */
    YELLOW("\u001B[33m"),
    /**
     * Cyan color.
     */
    CYAN("\u001B[36m");

    private final String color;

    /**
     * The LoggerColor constructor.
     * @param color The color of the logger.
     */
    LoggerColor(final String color) {
        this.color = color;
    }

    /**
     * Getter for the color.
     * @return The color of the logger.
     */
    @Override
    public String toString() {
        return this.color;
    }
}
