package io.github.yggdrasil80.yggtools.redis;

public enum IPatternDefault implements IPattern {

    DEFAULT("pattern:default");

    private final String pattern;

    IPatternDefault(final String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String getPattern() {
        return this.pattern;
    }
}
