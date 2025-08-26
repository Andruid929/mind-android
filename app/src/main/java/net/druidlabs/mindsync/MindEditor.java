package net.druidlabs.mindsync;

import android.app.Application;

import net.druidlabs.mindsync.preferences.AppPreferences;
import net.druidlabs.mindsync.util.UiUtil;

/**
 * Class to specify what should happen before the application is loaded.
 *
 * @author Andrew Jones
 * @version 1.0
 * @since 1.2.0-beta.2
 */

public class MindEditor extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        int appThemeIndex = AppPreferences.getAppThemeIndex(getApplicationContext());

        UiUtil.setApplicationTheme(appThemeIndex);
    }
}
