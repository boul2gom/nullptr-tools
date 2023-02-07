package io.github.nullptr.tools.platform;

public class PlatformHelper {

    public static final String OS = System.getProperty("os.name", "").toLowerCase();

    public static void exit(int exitCode, boolean halt) {
        if (halt) Runtime.getRuntime().halt(exitCode);
        else System.exit(exitCode);
    }

    public static boolean isOn(PlatformOS platform) {
        for (String alias : platform.getNames()) {
            if (OS.contains(alias)) {
                return true;
            }
        }
        return false;
    }

    public static PlatformOS getCurrentPlatform() {
        for (final PlatformOS platform : PlatformOS.values()) {
            for (final String name : platform.getNames()) {
                if (OS.contains(name)) {
                    return platform;
                }
            }
        }

        return null;
    }

    public static String getArch() {
        return System.getProperty("sun.arch.data.model");
    }
}
