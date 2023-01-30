package io.github.nullptr.tools.logger;

import io.github.nullptr.tools.io.FileWriter;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The file logger.
 */
public class FileLogger extends FileWriter {

    /**
     * The logger prefix.
     */
    private final String prefix;

    /**
     * The File Logger constructor.
     * @param path The path of the file to log to.
     * @param prefix The prefix of the logger.
     */
    public FileLogger(final Path path, final String prefix) {
        super(path);
        this.prefix = "[" + prefix + "] ";
    }

    /**
     * Logs a message.
     * @param type The type of the message.
     * @param message The message to log.
     */
    private void message(final LoggerPrintType type, final String message) {
        final String msg = String.format("[%s] ", new SimpleDateFormat("hh:mm:ss").format(new Date())) + this.prefix + "[" + type + "]: " + message;
        this.write(type.getColor() + msg + LoggerColor.RESET);
    }

    /**
     * Log as info.
     * @param message The message to log.
     */
    public void info(final String message) {
        this.message(LoggerPrintType.INFO, message);
    }

    /**
     * Log as error.
     * @param message The message to log.
     */
    public void error(final String message) {
        this.message(LoggerPrintType.ERROR, message);
    }

    /**
     * Log as error.
     * @param throwable The exception to log.
     */
    public void error(final Throwable throwable) {
        this.message(LoggerPrintType.ERROR, throwable.getMessage());
    }

    /**
     * Log as warning.
     * @param message The message to log.
     */
    public void warn(final String message) {
        this.message(LoggerPrintType.WARNING, message);
    }

    /**
     * Log as debug.
     * @param message The message to log.
     */
    public void debug(final String message) {
        this.message(LoggerPrintType.DEBUG, message);
    }
}
