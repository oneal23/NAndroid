package com.na.app;

import com.na.base.BaseApplication;
import com.na.utils.LogUtil;

public class NaApp extends BaseApplication {
    private static final String TAG = "NaApp";

    @Override
    protected void onInit() {
        super.onInit();
        LogUtil.init(TAG);
    }
}
