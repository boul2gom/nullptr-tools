package io.github.nullptr.tools.platform;

public enum PlatformOS {

    MAC("mac", "osx", "macos", "darwin"),
    WINDOWS("windows", "win"),
    LINUX("linux", "unix");

    private final String[] names;

    PlatformOS(String... names) {
        this.names = names;
    }

    public String[] getNames() {
        return this.names;
    }
}
