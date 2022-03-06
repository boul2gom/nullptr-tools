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
public final class Logger {

    private final boolean useFile;
    private final String prefix;
    private PrintWriter writer;

    /**
     * The Logger constructor.
     * @param prefix The prefix of the logger.
     */
    public Logger(final String prefix) {
        this(false, null, prefix);
    }

    /**
     * The Logger constructor.
     * @param useFile <code>true</code> if the logger should log to a file.
     * @param path The path of the file to log to.
     * @param prefix The prefix of the logger.
     */
    public Logger(final boolean useFile, final Path path, final String prefix) {
        this.prefix = "[" + prefix + "] ";
        this.useFile = useFile;

        if (this.useFile) {
            try {
                if (Files.notExists(path)) {
                    Files.createDirectories(path.getParent());
                    Files.createFile(path);
                }

                this.writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(path), StandardCharsets.UTF_8)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Logs a message.
     * @param type The type of the message.
     * @param message The message to log.
     */
    private void message(final LoggerPrintType type, final String message) {
        final String msg = String.format("[%s] ", new SimpleDateFormat("hh:mm:ss").format(new Date())) + this.prefix + "[" + type + "]: " + message;
        System.out.println(type.getColor() + msg + LoggerColor.RESET);
        this.logToFile(msg);
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
        if (this.useFile) {
            this.writer.println(message);
            this.writer.flush();
        }
    }

    /**
     * Closes the logger.
     */
    public void close() {
        this.writer.close();
    }
}
