package com.na.utils;

import com.na.utils.rx.AppSchedulerProvider;
import com.na.utils.rx.SchedulerProvider;

import io.reactivex.Scheduler;

public class SchedulerProviderUtil {

    private static volatile SchedulerProviderUtil singleton;
    private SchedulerProvider schedulerProvider;

    private SchedulerProviderUtil() {
        schedulerProvider = new AppSchedulerProvider();
    }

    public static SchedulerProviderUtil getInstance() {
        if (singleton == null) {
            synchronized (SchedulerProviderUtil.class) {
                if (singleton == null) {
                    singleton = new SchedulerProviderUtil();
                }
            }
        }
        return singleton;
    }

    private static SchedulerProvider getSchedulerProvider() {
        return getInstance().schedulerProvider;
    }

    public static Scheduler ui() {
        return getSchedulerProvider().ui();
    }


    public static Scheduler computation() {
        return getSchedulerProvider().computation();
    }


    public static Scheduler io() {
        return getSchedulerProvider().io();
    }
}