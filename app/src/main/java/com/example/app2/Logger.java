package com.example.app2;

import android.util.Log;

public class Logger {
    public static void log(String tag, String content) {
        if (content.length() > 4000) {
            Log.i(tag, content.substring(0, 4000));
            Logger.log(tag, content.substring(4000));
        } else {
            Log.i(tag, content);
        }
    }
}