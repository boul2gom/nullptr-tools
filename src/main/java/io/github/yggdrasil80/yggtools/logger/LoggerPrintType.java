package io.github.yggdrasil80.yggtools.logger;

public enum LoggerPrintType {

    INFO(LoggerColor.RESET),
    DEBUG(LoggerColor.CYAN),
    ERROR(LoggerColor.RED),
    WARNING(LoggerColor.YELLOW);

    private final LoggerColor color;

    LoggerPrintType(LoggerColor color) {
        this.color = color;
    }

    public LoggerColor getColor() {
        return this.color;
    }
}
