package net.druidlabs.mindsync.preferences;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import net.druidlabs.mindsync.R;
import net.druidlabs.mindsync.util.TimeStamp;
import net.druidlabs.mindsync.util.UiUtil;

/**
 * Fragment to handle application preferences.
 *
 * @author Andrew Jones
 * @version 1.0
 * @since 1.2.0-beta.1
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        String SETTINGS_FRAGMENT_LOG_TAG = "SettingsFragment";

        ListPreference appThemeListPref = findPreference(AppPreferences.APP_THEME_KEY);

        ListPreference timestampFormatListPref = findPreference(AppPreferences.NOTE_TIMESTAMP_FORMAT_KEY);

        SwitchPreferenceCompat blankHeaderSwitchPref = findPreference(AppPreferences.BLANK_HEADING_TOAST_KEY);

        SwitchPreferenceCompat blackThemeEnabledPref = findPreference(AppPreferences.BLACK_THEME_ENABLED_KEY);

        if (appThemeListPref != null) {
            appThemeListPref.setOnPreferenceChangeListener((preference, newValue) -> {

                int newPrefValue = Integer.parseInt(String.valueOf(newValue));

                UiUtil.setApplicationTheme(newPrefValue);

                Log.d(SETTINGS_FRAGMENT_LOG_TAG, "App theme changed to " + getAppThemeName(newPrefValue));

                Intent recreateIntent = requireActivity().getIntent();
                requireActivity().finish();
                requireActivity().overridePendingTransition(0, 0);
                requireActivity().startActivity(recreateIntent);

                return true;
            });
        }

        if (blackThemeEnabledPref != null) {
            blackThemeEnabledPref.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean newPrefValue = (boolean) newValue;

                String outputText = "Black theme " + (newPrefValue ? "enabled" : "disabled");

                Log.d(SETTINGS_FRAGMENT_LOG_TAG, outputText);

                return true;
            });

            int appThemeIndex = AppPreferences.getAppThemeIndex(requireContext());

            blackThemeEnabledPref.setVisible(appThemeIndex != 1);
        }

        if (timestampFormatListPref != null) {

            timestampFormatListPref.setOnPreferenceChangeListener((preference, newValue) -> {
                int newPrefValue = Integer.parseInt(String.valueOf(newValue));

                Log.d(SETTINGS_FRAGMENT_LOG_TAG, "Timestamp format changed to: " + TimeStamp.TIMESTAMP_FORMATS[newPrefValue]);

                return true;
            });
        }

        if (blankHeaderSwitchPref != null) {

            blankHeaderSwitchPref.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean newPrefValue = (boolean) newValue;

                String outputText = "Blank note heading toast " + (newPrefValue ? "enabled" : "disabled");

                Log.d(SETTINGS_FRAGMENT_LOG_TAG, outputText);

                return true;
            });
        }

    }

    /**
     * Get the theme from saved preferences.
     * <p>This value corresponds to an index from {@code R.array.prefs_ui_choose_theme_values}.
     *
     * @param appThemeIndex the current theme index.
     * @return current app theme that corresponds with the {@code appThemeIndex}
     */

    private String getAppThemeName(int appThemeIndex) {
        switch (appThemeIndex) {
            case 0:
                return "follow system theme";
            case 1:
                return "light theme";
            case 2:
                return "dark theme";
            case 3:
                return "black theme";
            default:
                throw new IllegalArgumentException("Unexpected theme index: " + appThemeIndex);
        }
    }
}