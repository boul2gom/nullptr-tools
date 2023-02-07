package io.github.nullptr.tools.io;

import java.nio.file.Path;

public class InstantFile extends FileWriter {

    private final String content;

    /**
     * Constructs a new InstantFile.
     *
     * @param path The path to the file.
     */
    public InstantFile(Path path, String content) {
        super(path);

        this.content = content;
    }

    public void write() {
        super.write(content);
        super.close();
    }
}
