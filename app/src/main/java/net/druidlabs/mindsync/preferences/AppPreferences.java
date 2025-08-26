package net.druidlabs.mindsync.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import net.druidlabs.mindsync.util.TimeStamp;

/**
 * Utility class for access to application preferences across activities.
 *
 * @author Andrew Jones
 * @version 1.0
 * @since 1.2.0-beta.1
 */

public final class AppPreferences {

    /**
     * Key for getting the current app theme preference.
     */

    static final String APP_THEME_KEY = "prefs_choose_theme";

    static final String BLACK_THEME_ENABLED_KEY = "prefs_enable_black_theme";

    /**
     * Key for getting the blank heading notification preference.
     */

    static final String BLANK_HEADING_TOAST_KEY = "prefs_blank_header_toast_disable";

    /**
     * Key for getting the note timestamp format preference.
     */

    static final String NOTE_TIMESTAMP_FORMAT_KEY = "prefs_note_timestamp_format";

    private AppPreferences() {
    }

    /**
     * Get the default shared preferences from the specified context.
     *
     * @param context the context from which to get the shared preferences.
     * @return default shared preferences.
     */

    private static SharedPreferences getPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Get the index of the current app theme.
     * <p>Values will range from 0-2 where
     * <ul>
     *     <li>{@code 0} will mean <strong>Following system theme</strong></li>
     *     <li>{@code 1} will mean <strong>Light theme</strong></li>
     *     <li>{@code 2} will mean <strong>Dark theme</strong></li>
     * </ul>
     *
     * @param context the context from which to get the shared preferences.
     * @return default shared preferences.
     */

    public static int getAppThemeIndex(Context context) {
        String systemThemeIndexString = getPrefs(context).getString(APP_THEME_KEY, "0");

        return Integer.parseInt(systemThemeIndexString);
    }

    /**
     * Get whether the black theme is enabled or not.
     *
     * @param context the context from which to get the shared preferences.
     * @return {@code true} if the setting is toggled on.
     */

    public static boolean isBlackThemeEnabled(Context context) {
        return getPrefs(context).getBoolean(BLACK_THEME_ENABLED_KEY, false);
    }

    /**
     * Get whether the blank note heading notification is enabled or not.
     *
     * @param context the context from which to get the shared preferences.
     * @return {@code true} if the setting is toggled on.
     */

    public static boolean isBlankHeadingToastEnabled(Context context) {
        return getPrefs(context).getBoolean(BLANK_HEADING_TOAST_KEY, false);
    }

    /**
     * Get the timestamp format set in the preference.
     * <p>The available formats are defined {@link TimeStamp#TIMESTAMP_FORMATS here}
     *
     * @param context the context from which to get the shared preferences.
     * @return default shared preferences.
     */

    public static String noteTimeStampFormat(Context context) {
        String formatIndexString = getPrefs(context).getString(NOTE_TIMESTAMP_FORMAT_KEY, "0");
        int formatIndex = Integer.parseInt(formatIndexString);

        return TimeStamp.TIMESTAMP_FORMATS[formatIndex];
    }
}