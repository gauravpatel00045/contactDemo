package com.contactdemo.interfaces;


import com.contactdemo.enums.RequestCode;

public interface RequestListener {
    /**
     * Called when a request completes with the given response. Executed by a
     * background thread: do not update the UI in this method.
     */
    void onRequestComplete(RequestCode requestCode, Object object);

    /**
     * Called when a request has a network or request error. Executed by a
     * background thread: do not update the UI in this method.
     */
    void onRequestError(String error, String status, RequestCode requestCode);
}
