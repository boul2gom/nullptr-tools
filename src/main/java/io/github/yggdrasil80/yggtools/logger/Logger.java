package io.github.yggdrasil80.yggtools.logger;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Logger {

    private final boolean useFile;
    private final String prefix;
    private PrintWriter writer;

    public Logger(final String prefix) {
        this(false, null, prefix);
    }

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

    private void message(final LoggerPrintType type, final String message) {
        final String msg = String.format("[%s] ", new SimpleDateFormat("hh:mm:ss").format(new Date())) + this.prefix + "[" + type + "]: " + message;
        System.out.println(type.getColor() + msg + LoggerColor.RESET);
        this.logToFile(msg);
    }

    public void info(final String message) {
        this.message(LoggerPrintType.INFO, message);
    }

    public void error(final String message) {
        this.message(LoggerPrintType.ERROR, message);
    }

    public void error(final Throwable throwable) {
        this.message(LoggerPrintType.ERROR, throwable.getMessage());
    }

    public void warn(final String message) {
        this.message(LoggerPrintType.WARNING, message);
    }

    public void debug(final String message) {
        this.message(LoggerPrintType.DEBUG, message);
    }

    private void logToFile(final String message) {
        if (this.useFile) {
            this.writer.println(message);
            this.writer.flush();
        }
    }

    public void close() {
        this.writer.close();
    }
}
