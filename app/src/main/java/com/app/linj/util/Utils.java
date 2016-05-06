package com.app.linj.util;

import android.util.Log;

/**
 * Created by zhangshenglan on 16/5/6.
 */
public class Utils {

    static final String LOG_TAG = "PullToRefresh";

    public static void warnDeprecation(String depreacted, String replacement) {
        Log.w(LOG_TAG, "You're using the deprecated " + depreacted + " attr, please switch over to " + replacement);
    }

}
