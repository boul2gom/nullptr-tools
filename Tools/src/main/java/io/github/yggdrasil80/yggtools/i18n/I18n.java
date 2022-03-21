package io.github.yggdrasil80.yggtools.i18n;

import java.util.IllegalFormatException;
import java.util.Locale;
import java.util.Map;

/**
 * This class provides internationalization support.
 */
public class I18n {

    private static I18n instance;

    private Locale locale;
    private final Map<Locale, Map<String, String>> languages;

    /**
     * The I18n constructor.
     * @param languages The languages, by locale.
     */
    I18n(Map<Locale, Map<String, String>> languages) {
        this.locale = new Locale("en_US");
        this.languages = languages;
        I18n.instance = this;
    }

    /**
     * Get a key value.
     * @param key The key.
     * @param replacements Optional replacements for formatting.
     * @return The value associated to the key.
     */
    public String get(String key, Object... replacements) {
        return this.get(this.locale, key, replacements);
    }

    /**
     * Get a key value.
     * @param locale The locale.
     * @param key The key.
     * @param replacements Optional replacements for formatting.
     * @return The value associated to the key, of the given locale.
     */
    public String get(Locale locale, String key, Object... replacements) {
        final String value = this.languages.get(locale).getOrDefault(key, key);
        try {
            return String.format(value, replacements);
        } catch (IllegalFormatException e) {
            return "Format error: " + value;
        }
    }

    /**
     * Set the locale.
      * @param locale The locale.
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * Get the I18n instance.
     * @return The I18n instance.
     */
    public static I18n getInstance() {
        if (instance == null) {
            throw new IllegalStateException("I18n is not initialized !");
        } else return instance;
    }
}
