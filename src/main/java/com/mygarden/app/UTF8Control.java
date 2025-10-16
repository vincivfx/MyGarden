package com.mygarden.app;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * ResourceBundle.Control implementation that reads .properties files using UTF-8.
 * Useful for non-ASCII characters (å, ä, ö).
 */
public class UTF8Control extends ResourceBundle.Control {
    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format,
                                    ClassLoader loader, boolean reload)
            throws IllegalAccessException, InstantiationException, IOException {

        String bundleName = toBundleName(baseName, locale);
        String resourceName = toResourceName(bundleName, "properties");

        try (var stream = loader.getResourceAsStream(resourceName)) {
            if (stream == null) return null;
            try (var reader = new InputStreamReader(stream, java.nio.charset.StandardCharsets.UTF_8)) {
                return new PropertyResourceBundle(reader);
            }
        }
    }
}
