package io.github.yggdrasil80.yggtools.redis;

/**
 * Default implementation of {@link IPattern}.
 */
public enum IPatternDefault implements IPattern {

    /**
     * The default pattern.
     */
    DEFAULT("pattern:default");

    private final String pattern;

    /**
     * The IPatternDefault constructor.
     * @param pattern The pattern name.
     */
    IPatternDefault(final String pattern) {
        this.pattern = pattern;
    }

    /**
     * {@inheritDoc}
     * @return The pattern name.
     */
    @Override
    public String getPattern() {
        return this.pattern;
    }
}
