package com.wl.placefinder.utils;

import android.util.Log;

/**
 */

public class Logger {
    private static final String TAG = "WLPF";

    public static void e(String tag, String message, Throwable e) {
        Log.e(TAG + ":" + tag, message, e);
    }

    public static void d(String tag, String message) {
        Log.d(TAG + ":" + tag, message);
    }

    public static void w(String tag, String message) {
        Log.w(TAG + ":" + tag, message);
    }

    public static void i(String tag, String message) {
        Log.i(TAG + ":" + tag, message);
    }

    public static void v(String tag, String message) {
        Log.v(TAG + ":" + tag, message);
    }
}
