package io.github.nullptr.tools.string;

public class StringHelper {

    public static String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
