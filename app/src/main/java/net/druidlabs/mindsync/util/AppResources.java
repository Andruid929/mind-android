package net.druidlabs.mindsync.util;

import android.content.Context;

/**
 * Utility class for providing easy access to application resources
 * by providing the resources as Java objects.
 *
 * @author Andrew Jones
 * @version 1.0
 * @since 1.1.0-beta.2
 */

public final class AppResources {

    private AppResources() {
    }

    /**
     * Get the string resource from the specific resource ID.
     * This method used to be a private helper class in {@code NoteEditorActivity}.
     *
     * @param resId   the string resource ID.
     * @param context the context from which to get the resource.
     * @return the String represented by the {@code resID}.
     * @since 0.7.0
     */

    public static String getStringResource(int resId, Context context) {
        return context.getResources().getString(resId);
    }

}
