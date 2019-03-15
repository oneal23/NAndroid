package com.na.base;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

public abstract class BaseApplication extends MultiDexApplication {
    private static BaseApplication mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        onInit();
    }

    public static BaseApplication getApp() {
        return mApp;
    }

    public static Context getContext() {
        return mApp.getApplicationContext();
    }

    protected void onInit() {

    }

}
