package com.na.base;

import android.app.Application;
import android.content.Context;

public class BaseAppliction extends Application {
    private static BaseAppliction mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
    }

    public static BaseAppliction getApp() {
        return mApp;
    }

    public static Context getContext() {
        return mApp.getApplicationContext();
    }
}
