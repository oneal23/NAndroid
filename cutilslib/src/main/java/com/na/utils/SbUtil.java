package com.na.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;

import com.jaeger.library.StatusBarUtil;

public class SbUtil {

    public static void setColor(Activity activity, @ColorInt int color, @IntRange(from = 0, to = 255) int statusBarAlpha) {
        StatusBarUtil.setColor(activity, color, statusBarAlpha);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void setLightMode(Activity activity) {
        StatusBarUtil.setLightMode(activity);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void setDarkMode(Activity activity) {
        StatusBarUtil.setDarkMode(activity);
    }
}
