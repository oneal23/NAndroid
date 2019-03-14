package com.na.base;

import android.app.Activity;

import com.na.utils.LogUtil;

import java.lang.ref.WeakReference;
import java.util.Stack;

/**
 * Created by oneal23 on 2018/6/26.
 */
public class AppManager {

    private final static String TAG = "AppManager";

    private Stack<WeakReference<Activity>> activityStack;

    public static class AppManagerHolder {
        public final static AppManager INSTANCE = new AppManager();
    }

    private AppManager() {

    }

    public static AppManager getInstance() {
        return AppManagerHolder.INSTANCE;
    }

    private void addActivity(WeakReference<Activity> weakRef) {
        if (activityStack == null) {
            activityStack = new Stack<WeakReference<Activity>>();
        }

        if (weakRef != null) {
            activityStack.add(weakRef);
        }
    }

    private void removeActivity(WeakReference<Activity> weakRef) {
        if (weakRef != null && activityStack != null && !activityStack.isEmpty()) {
            activityStack.remove(weakRef);
        }
    }

    private WeakReference<Activity> getActivityWeakRef(Activity activity) {
        WeakReference<Activity> weakRef = null;
        if (activity != null && activityStack != null && !activityStack.isEmpty()) {
            for (WeakReference<Activity> weakReference : activityStack) {
                if (null != weakReference.get() && activity.equals(weakReference.get())) {
                    weakRef = weakReference;
                    break;
                }
            }
        }
        return weakRef;
    }

    public void addActivity(Activity activity) {
        if (activity != null) {
            LogUtil.d(TAG, "addActivity:" + activity.getClass().getName());
            WeakReference<Activity> weakReference = new WeakReference(activity);
            addActivity(weakReference);
        }
    }

    public void removeActivity(Activity activity) {
        if (activity != null) {
            LogUtil.d(TAG, "removeActivity:" + activity.getClass().getName());
            removeActivity(getActivityWeakRef(activity));
        }
    }

    public Activity currentActivity() {
        if (activityStack != null && !activityStack.isEmpty()) {
            WeakReference<Activity> weakReference = activityStack.lastElement();
            return weakReference.get();
        }
        return null;
    }

    public void finishActivity(Activity activity) {
        WeakReference<Activity> weakRef = getActivityWeakRef(activity);
        if (weakRef != null) {
            if (weakRef.get() != null) {
                weakRef.get().finish();
            }
            removeActivity(weakRef);
        }
    }

    public void finishActivity(Class<?> cls) {
        if (cls != null && activityStack != null && !activityStack.isEmpty()) {
            for (WeakReference<Activity> weakReference : activityStack) {
                if (null != weakReference.get() && weakReference.get().getClass().equals(cls)) {
                    weakReference.get().finish();
                    removeActivity(weakReference);
                }
            }
        }
    }

    public void finishALLExcludeCurrentActivity(Class<?> cls) {
        if (activityStack != null) {
            for (WeakReference<Activity> weakReference : activityStack) {
                if (null != weakReference.get() && !weakReference.get().getClass().equals(cls)) {
                    weakReference.get().finish();
                    removeActivity(weakReference);
                }
            }
        }
    }

    public void finishALLExcludeCurrentActivity() {
        if (activityStack != null) {
            for (WeakReference<Activity> weakReference : activityStack) {
                if (null != weakReference.get() && !weakReference.get().equals(currentActivity())) {
                    weakReference.get().finish();
                    removeActivity(weakReference);
                }
            }
        }
    }


    public void finishAllActivity() {
        if (activityStack != null) {
            for (WeakReference<Activity> weakReference : activityStack) {
                if (null != weakReference.get()) {
                    weakReference.get().finish();
                }
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用
     */
    public void AppExit() {
        try {
            finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {

        }
    }
}
