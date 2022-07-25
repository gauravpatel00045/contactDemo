package com.contactdemo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.contactdemo.MyApplication;

public class Utils {

    /**
     * This method identify touch event. If user touches outside of keyboard
     * on screen instead of input control then call hide Keyboard method to hide keyboard.
     *
     * @param view (View) :parent view
     */
    @SuppressLint("ClickableViewAccessibility")
    public static void setupOutSideTouchHideKeyboard(final View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(view);
                    return false;
                }

            });
        }

        // If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);
                setupOutSideTouchHideKeyboard(innerView);
            }
        }
    }

    /**
     * This method hide keyboard
     *
     * @param view (View) : it contains view, on which touch event has been performed.
     */
    private static void hideKeyboard(View view) {

        InputMethodManager mgr = (InputMethodManager) MyApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    /**
     * This method gives the internet connection status
     * whether it is ON or OFF
     *
     * @return (boolean) : it return true or false if internet connection available or not, respectively
     * @see NetworkInfo#isConnectedOrConnecting()
     */
    public static boolean isInternetAvailable() {

        ConnectivityManager connectivity = (ConnectivityManager) MyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivity != null) {
            activeNetworkInfo = connectivity.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
