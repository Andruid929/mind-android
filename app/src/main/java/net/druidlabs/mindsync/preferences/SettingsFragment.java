package net.druidlabs.mindsync.preferences;

import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import net.druidlabs.mindsync.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        ListPreference chooseThemePref = findPreference("prefs_choose_theme");
        ListPreference pickTimestampPref = findPreference("prefs_note_timestamp_format");
        SwitchPreferenceCompat enabledBlackBgPref = findPreference("prefs_enable_amoled_dark_mode");
        SwitchPreferenceCompat disableBlankToast = findPreference("prefs_blank_toast_disable");

    }
}