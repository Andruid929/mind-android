package net.druidlabs.mindsync.util;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import androidx.appcompat.app.AppCompatDelegate;

public final class UiUtil {

    private UiUtil() {
    }

    public static void setApplicationTheme(int appThemeIndex) {
        if (appThemeIndex == 0) {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM);
        } else if (appThemeIndex == 1) {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO);
        } else if (appThemeIndex == 2) {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
        }
    }

}
