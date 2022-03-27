package io.github.yggdrasil80.yggtools.docker.dockerfile;

import io.github.yggdrasil80.yggtools.io.FileWriter;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates a Dockerfile.
 */
public class DockerfileGenerator extends FileWriter {

    /**
     * List of instructions to be written to the Dockerfile.
     */
    private final List<String> instructions;

    /**
     * The dockerfile generator constructor.
     * @param path The path to the Dockerfile.
     */
    public DockerfileGenerator(Path path) {
        super(path);
        this.instructions = new ArrayList<>();
    }

    /**
     * Add a FROM instruction. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param from The FROM instruction.
     * @param comment The comment to be added above the instruction (optional).
     */
    public void withFrom(@NotNull String from, String comment) {
        this.with(DockerInstruction.FROM, from, comment);
    }

    /**
     * Add a MAINTAINER instruction. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param maintainer The MAINTAINER instruction.
     * @param comment The comment to be added above the instruction (optional).
     */
    public void withMaintainer(@NotNull String maintainer, String comment) {
        this.with(DockerInstruction.MAINTAINER, maintainer, comment);
    }

    /**
     * Add a RUN instruction. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param run The RUN instruction.
     * @param comment The comment to be added above the instruction (optional).
     */
    public void withRun(@NotNull String run, String comment) {
        this.with(DockerInstruction.RUN, run, comment);
    }

    /**
     * Add a CMD instruction. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param cmd The CMD instruction.
     * @param comment The comment to be added above the instruction (optional).
     */
    public void withCmd(@NotNull String cmd, String comment) {
        this.with(DockerInstruction.CMD, cmd, comment);
    }

    /**
     * Add a LABEL instruction. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param label The LABEL instruction.
     * @param comment The comment to be added above the instruction (optional).
     */
    public void withLabel(@NotNull String label, String comment) {
        this.with(DockerInstruction.LABEL, label, comment);
    }

    /**
     * Add an ENV instruction. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param env The ENV instruction.
     * @param comment The comment to be added above the instruction (optional).
     */
    public void withEnv(@NotNull String env, String comment) {
        this.with(DockerInstruction.ENV, env, comment);
    }

    /**
     * Add an EXPOSE instruction. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param expose The EXPOSE instruction.
     * @param comment The comment to be added above the instruction (optional).
     */
    public void withExpose(@NotNull String expose, String comment) {
        this.with(DockerInstruction.EXPOSE, expose, comment);
    }

    /**
     * Add an ADD instruction. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param add The ADD instruction.
     * @param comment The comment to be added above the instruction (optional).
     */
    public void withAdd(@NotNull String add, String comment) {
        this.with(DockerInstruction.ADD, add, comment);
    }

    /**
     * Add a COPY instruction. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param copy The COPY instruction.
     * @param comment The comment to be added above the instruction (optional).
     */
    public void withCopy(@NotNull String copy, String comment) {
        this.with(DockerInstruction.COPY, copy, comment);
    }

    /**
     * Add a ENTRYPOINT instruction. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param entryPoint The ENTRYPOINT instruction.
     * @param comment The comment to be added above the instruction (optional).
     */
    public void withEntryPoint(@NotNull String entryPoint, String comment) {
        this.with(DockerInstruction.ENTRYPOINT, entryPoint, comment);
    }

    /**
     * Add a VOLUME instruction. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param volume The VOLUME instruction.
     * @param comment The comment to be added above the instruction (optional).
     */
    public void withVolume(@NotNull String volume, String comment) {
        this.with(DockerInstruction.VOLUME, volume, comment);
    }

    /**
     * Add a USER instruction. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param user The USER instruction.
     * @param comment The comment to be added above the instruction (optional).
     */
    public void withUser(@NotNull String user, String comment) {
        this.with(DockerInstruction.USER, user, comment);
    }

    /**
     * Add a WORKDIR instruction. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param workdir The WORKDIR instruction.
     * @param comment The comment to be added above the instruction (optional).
     */
    public void withWorkdir(@NotNull String workdir, String comment) {
        this.with(DockerInstruction.WORKDIR, workdir, comment);
    }

    /**
     * Add a ARG instruction. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param arg The ARG instruction.
     * @param comment The comment to be added above the instruction (optional).
     */
    public void withArg(@NotNull String arg, String comment) {
        this.with(DockerInstruction.ARG, arg, comment);
    }

    /**
     * Add a Dockerfile instruction. <br>
     * /!\ If you don't want to use a param marked as optional, simply pass null.
     * @param instruction The Dockerfile instruction.
     * @param value The value of the instruction.
     * @param comment The comment to be added above the instruction (optional).
     */
    public void with(@NotNull DockerInstruction instruction, @NotNull String value, String comment) {
        if (comment != null) this.instructions.add("# " + comment);
        this.instructions.add(instruction.name() + " " + value + "\n");
    }

    /**
     * Generate the Dockerfile.
     */
    public void generate() {
        for (String instruction : this.instructions) {
            this.write(instruction);
        }
    }
}
