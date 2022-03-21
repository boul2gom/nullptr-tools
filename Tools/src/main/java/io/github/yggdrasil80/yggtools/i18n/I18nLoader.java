package io.github.yggdrasil80.yggtools.i18n;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import io.github.yggdrasil80.yggtools.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Loads kay/value pairs from a directory containing JSON files.
 */
public class I18nLoader {

    private static final Gson GSON = new Gson();
    private static final Logger LOGGER = new Logger("I18nLoader");

    /**
     * Loads kay/value pairs from a directory containing JSON files. <br>
     * All JSON files must be named with the Locale in the file name, for example "en_US.json" and "fr_FR.json".
     * @param path The path to the directory containing the JSON files.
     * @return The loaded kay/value pairs in an {@link I18n} instance.
     */
    public static I18n loadLanguages(String path) {
        final Map<Locale, Map<String, String>> languages = new HashMap<>();

        try {
            Files.walk(new File(path).toPath()).forEach(file -> {
                if (file.toFile().isDirectory()) return;
                if (!file.toFile().getName().endsWith(".json")) return;

                final Locale locale = new Locale(file.getFileName().toString().replace(".json", ""));
                final Map<String, String> lang = new HashMap<>();

                try {
                    final JsonReader reader = new JsonReader(new InputStreamReader(Files.newInputStream(file), StandardCharsets.UTF_8));
                    final Map<String, String> map = GSON.fromJson(reader, Map.class);

                    lang.putAll(map);
                    languages.put(locale, lang);

                    LOGGER.info("Loaded language file: " + file.getFileName().toString());
                } catch (IOException e) {
                    LOGGER.error("Failed to load language file " + file.getFileName().toString() + ": " + e.getMessage());
                }
            });

            return new I18n(languages);
        } catch (IOException e) {
            LOGGER.error("Failed to load languages: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
