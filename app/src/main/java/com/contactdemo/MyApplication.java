package com.contactdemo;

import android.app.Application;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MyApplication extends Application {

    // class object declaration
    public static MyApplication appInstance;
    public static Gson gson;
    private int socketTimeout = 30000; //30 seconds
    private RequestQueue mRequestQueue;
    /**
     * @return appInstance (MyApplication) : it return MyApplication instance
     */
    public static synchronized MyApplication getInstance() {
        return appInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appInstance = this;
        gson = new GsonBuilder().create();
    }

    //    Volley Related methods
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(String tag,Request<T> req) {
        req.setTag(tag);
        getRequestQueue().add(req);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public void cancelPendingRequests(String tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public <T> void setRequestTimeout(Request<T> req) {
        req.setRetryPolicy(new DefaultRetryPolicy(
                socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
