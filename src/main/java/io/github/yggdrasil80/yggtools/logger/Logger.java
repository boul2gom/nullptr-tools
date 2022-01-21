package io.github.yggdrasil80.yggtools.logger;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private final boolean file;
    private final String prefix;
    private PrintWriter writer;

    public Logger(String prefix) {
        this(false, null, prefix);
    }

    public Logger(boolean useFile, Path path, String prefix) {
        this.prefix = "[" + prefix + "] ";
        this.file = useFile;

        if (this.file) {
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

    private void message(LoggerPrintType type, String message) {
        final String msg = String.format("[%s] ", new SimpleDateFormat("hh:mm:ss").format(new Date())) + this.prefix + "[" + type + "]: " + message;
        System.out.println(type.getColor() + msg + LoggerColor.RESET);
        this.logToFile(msg);
    }

    public void info(String message) {
        this.message(LoggerPrintType.INFO, message);
    }

    public void error(String message) {
        this.message(LoggerPrintType.ERROR, message);
    }

    public void error(Throwable throwable) {
        this.message(LoggerPrintType.ERROR, throwable.getMessage());
    }

    public void warn(String message) {
        this.message(LoggerPrintType.WARNING, message);
    }

    public void debug(String message) {
        this.message(LoggerPrintType.DEBUG, message);
    }

    private void logToFile(String message) {
        if (this.file) {
            this.writer.println(message);
            this.writer.flush();
        }
    }

    public void close() {
        this.writer.close();
    }
}
