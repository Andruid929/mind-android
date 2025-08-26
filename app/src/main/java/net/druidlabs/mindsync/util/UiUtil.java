package net.druidlabs.mindsync.util;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import net.druidlabs.mindsync.R;
import net.druidlabs.mindsync.preferences.AppPreferences;

/**
 * <strong>Util</strong>ity class for handling UI changes and states.
 *
 * @author Andrew JOnes
 * @version 1.0
 * @since 1.2.0-beta.2
 */

public final class UiUtil {

    private static final String UI_UTIL_TAG = "UI Utility";

    private UiUtil() {
    }

    /**
     * Changes the application's theme
     * by setting the {@link AppCompatDelegate#setDefaultNightMode(int)  default night mode}
     * depending on the app theme index.
     * <p>
     * <ul>
     *     <li>{@code 0} sets {@link AppCompatDelegate#MODE_NIGHT_FOLLOW_SYSTEM follow system theme}</li>
     *     <li>{@code 1} sets {@link AppCompatDelegate#MODE_NIGHT_NO light mode}</li>
     *     <li>{@code 2} sets {@link AppCompatDelegate#MODE_NIGHT_YES dark mode}</li>
     * </ul>
     *
     * @param appThemeIndex the app's theme index, usually got by calling {@link AppPreferences#getAppThemeIndex(Context)}
     */

    public static void setApplicationTheme(int appThemeIndex) {
        if (appThemeIndex == 0) {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM);
            Log.d(UI_UTIL_TAG, "App now following system theme");

        } else if (appThemeIndex == 1) {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO);
            Log.d(UI_UTIL_TAG, "App now set to light mode");

        } else if (appThemeIndex == 2) {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
            Log.d(UI_UTIL_TAG, "App now set to dark mode");
        }
    }

    /**
     * Sets the black theme to any activity passed in as a parameter.
     * <p>The activity theme is only set to black when the black theme preference
     * is enabled, and the app theme is set to dark mode.
     *
     * @param activity the activity to apply the theme to.
     */

    public static void setBlackMode(AppCompatActivity activity) {
        if (AppPreferences.isBlackThemeEnabled(activity) && isDarkModeEnabled(activity)) {
            //Set black theme

            activity.setTheme(R.style.Base_Theme_MindSync20_Black);

            Log.d(UI_UTIL_TAG, "Black background applied to: " + activity.getComponentName().getClassName());

        } else if (isDarkModeEnabled(activity)) {
            //Set dark theme

            activity.setTheme(R.style.Base_Theme_MindSync20);

            Log.d(UI_UTIL_TAG, "Dark background applied to: " + activity.getComponentName().getClassName());
        }
    }

    /**
     * Checks to see if the app is in dark mode.
     * <p>Note that this works whether the theme is set to
     * always be dark mode or is following the system theme.
     *
     * @param context UI context to check theme from.
     * @return {@code true} if the app theme is dark mode,
     * otherwise {@code false};
     */

    private static boolean isDarkModeEnabled(Context context) {
        int currentMode = context.getResources().getConfiguration().uiMode;

        int nightModeFlag = currentMode & Configuration.UI_MODE_NIGHT_MASK;

        return nightModeFlag == Configuration.UI_MODE_NIGHT_YES;
    }

}
