package io.github.yggdrasil80.yggtools.logger;

/**
 * Enum for the different types of log messages.
 */
public enum LoggerPrintType {

    /**
     * The message is an info message.
     */
    INFO(LoggerColor.RESET),
    /**
     * The message is a debug message.
     */
    DEBUG(LoggerColor.CYAN),
    /**
     * The message is an error message.
     */
    ERROR(LoggerColor.RED),
    /**
     * The message is a warning message.
     */
    WARNING(LoggerColor.YELLOW);

    private final LoggerColor color;

    /**
     * The LoggerPrintType constructor.
     * @param color
     */
    LoggerPrintType(final LoggerColor color) {
        this.color = color;
    }

    /**
     * Get the color of the message.
     * @return The color of the message
     */
    public LoggerColor getColor() {
        return this.color;
    }
}
