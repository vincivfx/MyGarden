package com.mygarden.app;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * LanguageManager that supports:
 *  - a configurable list of supported locales
 *  - setting locale by Locale or languageTag
 *  - cycling to the next supported locale
 *  - a JavaFX ObjectProperty<Locale> to observe changes
 */
public final class LanguageManager {
    private static final String PREF_KEY = "app.locale";
    private static final String BASE_NAME = "i18n.messages";
    private static final Preferences prefs = Preferences.userNodeForPackage(LanguageManager.class);

    /** Add or remove supported locales here. Order matters for cycling. */
    private static final List<Locale> SUPPORTED_LOCALES = List.of(
            Locale.ENGLISH,
            new Locale("sv") // Swedish
    );

    // backing property so UI can observe changes
    private static final ObjectProperty<Locale> localeProperty =
            new SimpleObjectProperty<>(loadLocaleFromPrefs());

    private LanguageManager() {}

    private static Locale loadLocaleFromPrefs() {
        String tag = prefs.get(PREF_KEY, "");
        if (tag == null || tag.isBlank()) return SUPPORTED_LOCALES.get(0);
        try {
            Locale saved = Locale.forLanguageTag(tag);
            // If saved locale isn't supported, fall back to first supported locale
            return SUPPORTED_LOCALES.stream()
                    .filter(l -> l.getLanguage().equals(saved.getLanguage()))
                    .findFirst()
                    .orElse(SUPPORTED_LOCALES.get(0));
        } catch (Exception e) {
            return SUPPORTED_LOCALES.get(0);
        }
    }

    /** Return currently active locale. */
    public static Locale getLocale() {
        return localeProperty.get();
    }

    /** Return the current language code, e.g. "en", "sv". */
    public static String getCurrentLang() {
        return getLocale().getLanguage();
    }

    /** JavaFX property for binding/listening. */
    public static ObjectProperty<Locale> localeProperty() {
        return localeProperty;
    }

    /**
     * Set the application's locale. Persists the choice in Preferences and updates the property.
     */
    public static void setLocale(Locale locale) {
        Locale newLocale = (locale == null) ? SUPPORTED_LOCALES.get(0) : normalizeToSupported(locale);
        prefs.put(PREF_KEY, newLocale.toLanguageTag());
        localeProperty.set(newLocale);
    }

    /** Convenience: set locale by language tag like "sv" or "en". */
    public static void setLocale(String languageTag) {
        if (languageTag == null || languageTag.isBlank()) {
            setLocale(SUPPORTED_LOCALES.get(0));
            return;
        }
        setLocale(Locale.forLanguageTag(languageTag));
    }

    /** Return the ResourceBundle for the current locale (UTF8Control usage preserved if you use it). */
    public static ResourceBundle getBundle() {
        // return ResourceBundle.getBundle(BASE_NAME, getLocale(), new UTF8Control());
        return ResourceBundle.getBundle(BASE_NAME, getLocale());
    }

    /**
     * Cycle to the next supported locale (wraps around). Useful for a toggle/cycle button.
     * Example: en -> sv -> en when only those two are present.
     */
    /*public static void cycleLocale() {
        Locale current = getLocale();
        int idx = SUPPORTED_LOCALES.indexOf(current);
        if (idx < 0) {
            // not found (shouldn't happen) — set to first supported
            setLocale(SUPPORTED_LOCALES.get(0));
            return;
        }
        int next = (idx + 1) % SUPPORTED_LOCALES.size();
        setLocale(SUPPORTED_LOCALES.get(next));
    }*/

    /** Return the list of supported locales (unmodifiable view). */
    public static List<Locale> getSupportedLocales() {
        return List.copyOf(SUPPORTED_LOCALES);
    }

    /** Normalize requested locale to a supported locale by language if possible. */
    private static Locale normalizeToSupported(Locale requested) {
        if (requested == null) return SUPPORTED_LOCALES.get(0);
        // try exact match first
        for (Locale l : SUPPORTED_LOCALES) {
            if (Objects.equals(l, requested)) return l;
        }
        // match by language only (en, sv, fr)
        for (Locale l : SUPPORTED_LOCALES) {
            if (l.getLanguage().equals(requested.getLanguage())) return l;
        }
        // fallback
        return SUPPORTED_LOCALES.get(0);
    }


}