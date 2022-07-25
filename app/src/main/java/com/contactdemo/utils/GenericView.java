package com.contactdemo.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;

/**
 * This class bind the xml view with activity
 * with custom methods
 */
public class GenericView {

    /**
     * Convenience method of findViewById
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T findViewById(final View parent, final int id) {
        return (T) parent.findViewById(id);
    }

    /**
     * Convenience method of findViewById
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T findViewById(final Activity activity, final int id) {
        return (T) activity.findViewById(id);
    }

    /**
     * Convenience method of findViewById
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T findViewById(final Dialog dialog, final int id) {
        return (T) dialog.findViewById(id);
    }

    /**
     * This method get the string values from resources
     *
     * @param context(Context) : context
     * @param id(int)          : resource id of string in String.xml file
     */
    public static String getString(final Context context, final int id) {
        return context.getResources().getString(id);
    }
}
