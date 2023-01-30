package io.github.nullptr.tools.io;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The class used to write to a file.
 */
public abstract class FileWriter implements AutoCloseable {

    /**
     * The writer.
     */
    private final PrintWriter writer;

    /**
     * Constructs a new FileWriter.
     * @param path The path to the file.
     */
    public FileWriter(Path path) {
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

    /**
     * Writes a line to the file.
     * @param toWrite The line to write.
     */
    public void write(String toWrite) {
        this.writer.println(toWrite);
        this.writer.flush();
    }

    /**
     * Closes the writer.
     */
    @Override
    public void close() {
        this.writer.close();
    }
}
