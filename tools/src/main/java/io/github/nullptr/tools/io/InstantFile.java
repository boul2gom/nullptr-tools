package io.github.nullptr.tools.io;

import java.nio.file.Path;

public class InstantFile extends FileWriter {

    /**
     * Constructs a new InstantFile.
     *
     * @param path The path to the file.
     */
    public InstantFile(Path path, String content) {
        super(path);

        super.write(content);
        super.close();
    }
}
