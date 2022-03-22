package io.github.yggdrasil80.yggtools.logger;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Logger class.
 */
public class FileLogger implements AutoCloseable {

    private final String prefix;
    private final PrintWriter writer;

    /**
     * The File Logger constructor.
     * @param path The path of the file to log to.
     * @param prefix The prefix of the logger.
     */
    public FileLogger(final Path path, final String prefix) {
        this.prefix = "[" + prefix + "] ";

        try {
            if (Files.notExists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }

            this.writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(path), StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    /**
     * Logs a message.
     * @param type The type of the message.
     * @param message The message to log.
     */
    private void message(final LoggerPrintType type, final String message) {
        final String msg = String.format("[%s] ", new SimpleDateFormat("hh:mm:ss").format(new Date())) + this.prefix + "[" + type + "]: " + message;
        this.logToFile(type.getColor() + msg + LoggerColor.RESET);
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

    /**
     * Logs a message to the file.
     * @param message The message to log.
     */
    private void logToFile(final String message) {
        this.writer.println(message);
        this.writer.flush();
    }

    /**
     * Closes the logger.
     */
    @Override
    public void close() {
        this.writer.close();
    }
}
