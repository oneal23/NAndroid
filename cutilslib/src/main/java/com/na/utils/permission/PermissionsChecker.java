package com.na.utils.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationManagerCompat;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oneal23 on 2018/6/26.
 */
public class PermissionsChecker {

    public static interface RequestCode {
        int CALENDAR = 10001;
        int CAMERA = 10002;
        int CONTACTS = 10003;
        int LOCATION = 10004;
        int MICROPHONE = 10005;
        int PHONE = 10006;
        int SENSORS = 10007;
        int SMS = 10008;
        int STORAGE = 10009;
        int STORAGE_AND_CAMERA = 30001;
        int DRAWOVERLAYS = 20001;
        int PERMISSION_SETTING = 20002;
    }

    public interface PermissionCallback {
        //请求权限成功
        void onPermissionsGranted(int requestCode);

        //请求权限失败
        void onPermissionsDenied(int requestCode);

        //请求权限失败不再提示
        void onPermissionsDeniedAlways(int requestCode);
    }

    public static class Permission {

        public static final String[] CALENDAR;
        public static final String[] CAMERA;
        public static final String[] CONTACTS;
        public static final String[] LOCATION;
        public static final String[] MICROPHONE;
        public static final String[] PHONE;
        public static final String[] SENSORS;
        public static final String[] SMS;
        public static final String[] STORAGE;

        static {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                CALENDAR = new String[]{};
                CAMERA = new String[]{};
                CONTACTS = new String[]{};
                LOCATION = new String[]{};
                MICROPHONE = new String[]{};
                PHONE = new String[]{};
                SENSORS = new String[]{};
                SMS = new String[]{};
                STORAGE = new String[]{};
            } else {
                CALENDAR = new String[]{
                        Manifest.permission.READ_CALENDAR,
                        Manifest.permission.WRITE_CALENDAR};

                CAMERA = new String[]{
                        Manifest.permission.CAMERA};

                CONTACTS = new String[]{
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.GET_ACCOUNTS};

                LOCATION = new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION};

                MICROPHONE = new String[]{
                        Manifest.permission.RECORD_AUDIO};

                PHONE = new String[]{
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.WRITE_CALL_LOG,
                        Manifest.permission.USE_SIP,
                        Manifest.permission.PROCESS_OUTGOING_CALLS};

                SENSORS = new String[]{
                        Manifest.permission.BODY_SENSORS};

                SMS = new String[]{
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.RECEIVE_WAP_PUSH,
                        Manifest.permission.RECEIVE_MMS};

                STORAGE = new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE};
            }
        }
    }

    private ArrayList<String> mPermissions;
    private int mRequestCode;
    private Object object;
    private static PermissionCallback permissionCallback;
    private static PermissionCallback setPermissionCallback;
    private static String[] setPermissions = null;

    private PermissionsChecker(Object object) {
        this.object = object;
    }

    public static PermissionsChecker with(Activity activity) {
        return new PermissionsChecker(activity);
    }

    public static PermissionsChecker with(Fragment fragment) {
        return new PermissionsChecker(fragment);
    }

    public PermissionsChecker permissions(String... permissions) {

        return addPermissions(permissions);
    }

    public PermissionsChecker addPermissions(String... permissions) {
        if (this.mPermissions == null) {
            this.mPermissions = new ArrayList<String>();
        }

        if (permissions != null && permissions.length > 0) {
            for (String p : permissions) {
                mPermissions.add(p);
            }
        }

        return this;
    }

    public String[] getPermissions() {
        if (this.mPermissions != null && mPermissions.size() > 0) {
            String[] permissions = new String[mPermissions.size()];
            this.mPermissions.toArray(permissions);
            return permissions;
        }
        return null;
    }

    public PermissionsChecker addRequestCode(int requestCode) {
        this.mRequestCode = requestCode;
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void request() {
        permissionCallback = null;
        requestPermissions(object, mRequestCode, getPermissions());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void request(PermissionCallback callback) {
        if (callback != null) {
            permissionCallback = callback;
        }
        requestPermissions(object, mRequestCode, getPermissions());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void needPermission(Activity activity, int requestCode, String[] permissions) {
        permissionCallback = null;
        requestPermissions(activity, requestCode, permissions);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void needPermission(Fragment fragment, int requestCode, String[] permissions) {
        permissionCallback = null;
        requestPermissions(fragment, requestCode, permissions);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void needPermission(Activity activity, int requestCode, String[] permissions, PermissionCallback callback) {
        if (callback != null) {
            permissionCallback = callback;
        }
        requestPermissions(activity, requestCode, permissions);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void needPermission(Fragment fragment, int requestCode, String[] permissions, PermissionCallback callback) {
        if (callback != null) {
            permissionCallback = callback;
        }
        requestPermissions(fragment, requestCode, permissions);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void needPermission(Activity activity, int requestCode, String permission) {
        permissionCallback = null;
        needPermission(activity, requestCode, new String[]{permission});
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void needPermission(Fragment fragment, int requestCode, String permission) {
        permissionCallback = null;
        needPermission(fragment, requestCode, new String[]{permission});
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void needPermission(Activity activity, int requestCode, String permission, PermissionCallback callback) {
        if (callback != null) {
            permissionCallback = callback;
        }
        needPermission(activity, requestCode, new String[]{permission});
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void needPermission(Fragment fragment, int requestCode, String permission, PermissionCallback callback) {
        if (callback != null) {
            permissionCallback = callback;
        }
        needPermission(fragment, requestCode, new String[]{permission});
    }

    public static boolean isPermissionsGranted(Object object, String... permissions) {
        boolean flag = true;
        if (PermissionsUtils.isOverMarshmallow()) {
            List<String> deniedPermissions = PermissionsUtils.findDeniedPermissions(PermissionsUtils.getActivity(object), permissions);
            if (deniedPermissions != null && deniedPermissions.size() > 0) {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * 请求权限
     *
     * @param object
     * @param requestCode
     * @param permissions
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void requestPermissions(Object object, int requestCode, String[] permissions) {
        if (!PermissionsUtils.isOverMarshmallow()) {
            if (permissionCallback != null) {
                permissionCallback.onPermissionsGranted(requestCode);
            } else {
                doExecuteGranted(object, requestCode);
            }
            return;
        }

        List<String> deniedPermissions = PermissionsUtils.findDeniedPermissions(PermissionsUtils.getActivity(object), permissions);
        /**
         * 先检查是否有没有授予的权限，有的话请求，没有的话就直接执行权限授予成功的接口/注解方法
         */
        if (deniedPermissions.size() > 0) {
            requestPermissionsImpl(object, requestCode, permissions);
        } else {
            if (permissionCallback != null) {
                permissionCallback.onPermissionsGranted(requestCode);
            } else {
                doExecuteGranted(object, requestCode);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void requestPermissionsImpl(Object object, int requestCode, String[] permissions) {
        if (object instanceof Activity) {
            ((Activity) object).requestPermissions(permissions, requestCode);
        } else if (object instanceof Fragment) {
            ((Fragment) object).requestPermissions(permissions, requestCode);
        } else {
            throw new IllegalArgumentException(object.getClass().getName() + " is not supported");
        }
    }

    private static void doExecuteGranted(Object activity, int requestCode) {
        Method executeMethod = PermissionsUtils.findMethodWithRequestCode(activity.getClass(), PermissionsGranted.class, requestCode);
        executeMethod(activity, executeMethod);
    }

    private static void doExecuteDenied(Object activity, int requestCode) {
        Method executeMethod = PermissionsUtils.findMethodWithRequestCode(activity.getClass(), PermissionsDenied.class, requestCode);
        executeMethod(activity, executeMethod);
    }

    private static void doExecuteDeniedAlways(Object activity, int requestCode) {
        Method executeMethod = PermissionsUtils.findMethodWithRequestCode(activity.getClass(), PermissionsDeniedAlways.class, requestCode);
        executeMethod(activity, executeMethod);
    }

    private static void executeMethod(Object activity, Method executeMethod) {
        if (executeMethod != null) {
            try {
                if (!executeMethod.isAccessible()) {
                    executeMethod.setAccessible(true);
                }
                Object object = null;
                executeMethod.invoke(activity, object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        requestResult(activity, requestCode, permissions, grantResults);
    }

    public static void onRequestPermissionsResult(Fragment fragment, int requestCode, String[] permissions, int[] grantResults) {
        requestResult(fragment, requestCode, permissions, grantResults);
    }

    /**
     * 有回调接口的话(即回调接口不为空的话)先执行回调接口的方法，若为空，则寻找响应的注解方法。
     *
     * @param obj
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    private static void requestResult(Object obj, int requestCode, String[] permissions, int[] grantResults) {
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            }
        }

        if (deniedPermissions.size() > 0) {
            Activity activity = PermissionsUtils.getActivity(obj);
            boolean isRationale = false;
            if (activity != null) {
                isRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, deniedPermissions.get(0));
            }

            if (permissionCallback != null) {
                if (isRationale) {
                    permissionCallback.onPermissionsDenied(requestCode);
                } else {
                    permissionCallback.onPermissionsDeniedAlways(requestCode);
                }
            } else {
                if (isRationale) {
                    doExecuteDenied(obj, requestCode);
                } else {
                    doExecuteDeniedAlways(obj, requestCode);
                }
            }
        } else {
            if (permissionCallback != null) {
                permissionCallback.onPermissionsGranted(requestCode);
            } else {
                doExecuteGranted(obj, requestCode);
            }
        }
    }

    public static void requestDrawOverlaysPermission(Object object, PermissionCallback callback) {
        if (callback != null) {
            permissionCallback = callback;
        }
        needDrawOverlaysPermission(object);
    }

    public static void needDrawOverlaysPermissionForObject(Object object) {
        needDrawOverlaysPermission(object);
    }

    public static void needDrawOverlaysPermissionForActivity(Activity activity) {
        needDrawOverlaysPermission(activity);
    }

    public static void needDrawOverlaysPermissionForFragemnt(Fragment fragment) {
        needDrawOverlaysPermission(fragment);
    }

    private static void needDrawOverlaysPermission(Object obj) {
        Activity activity = PermissionsUtils.getActivity(obj);
        if (!checkDrawOverlaysPermission(activity)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName()));
            activity.startActivityForResult(intent, RequestCode.DRAWOVERLAYS);
        }
    }

    public static boolean checkDrawOverlaysPermission(Context context) {
        boolean flag = false;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            flag = true;
        } else {
            flag = Settings.canDrawOverlays(context);
        }
        return flag;
    }


//    public static boolean checkDrawOverlaysPermission(Context context) {
//        boolean flag = false;
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//            flag = true;
//        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            try {
//                Class cls = Class.forName("android.content.Context");
//                Field declaredField = cls.getDeclaredField("APP_OPS_SERVICE");
//                declaredField.setAccessible(true);
//                Object obj = declaredField.get(cls);
//                String str2 = (String) obj;
//                obj = cls.getMethod("getSystemService", String.class).invoke(context, str2);
//                cls = Class.forName("android.app.AppOpsManager");
//                Field declaredField2 = cls.getDeclaredField("MODE_ALLOWED");
//                declaredField2.setAccessible(true);
//                Method checkOp = cls.getMethod("checkOp", Integer.TYPE, Integer.TYPE, String.class);
//                int result = (Integer) checkOp.invoke(obj, 24, Binder.getCallingUid(), context.getPackageName());
//                flag = (result == declaredField2.getInt(cls));
//            } catch (Exception e) {
//
//            }
//        } else {
//            AppOpsManager appOpsMgr = null;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                appOpsMgr = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
//            }
//
//            if (appOpsMgr != null) {
//                int mode = appOpsMgr.checkOpNoThrow("android:system_alert_window", android.os.Process.myUid(), context.getPackageName());
//                flag = (mode == AppOpsManager.MODE_ALLOWED || mode == AppOpsManager.MODE_IGNORED);
//            } else {
//                flag = Settings.canDrawOverlays(context);
//            }
//
//        }
//
//        return flag;
//    }

    public static void onActivityResult(Object obj, int requestCode, int resultCode, Intent data) {
        requestResult(obj, requestCode, resultCode, data);
    }

    private static void requestResult(Object obj, int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.DRAWOVERLAYS) {
            Activity activity = PermissionsUtils.getActivity(obj);
            if (!checkDrawOverlaysPermission(activity)) {
                if (permissionCallback != null) {
                    permissionCallback.onPermissionsDenied(requestCode);
                } else {
                    doExecuteDenied(obj, requestCode);
                }
            } else {
                if (permissionCallback != null) {
                    permissionCallback.onPermissionsGranted(requestCode);
                } else {
                    permissionCallback.onPermissionsGranted(requestCode);
                }
            }
        } else if (requestCode == RequestCode.PERMISSION_SETTING){
            if (setPermissions != null && setPermissions.length > 0) {
                boolean flag = isPermissionsGranted(obj, setPermissions);
                if (flag) {
                    if (permissionCallback != null) {
                        permissionCallback.onPermissionsGranted(RequestCode.PERMISSION_SETTING);
                    }
                } else {
                    if (permissionCallback != null) {
                        permissionCallback.onPermissionsDenied(RequestCode.PERMISSION_SETTING);
                    }
                }
            }

        }
    }

    public static void openSettingPermission(Object act, PermissionCallback callback, String... permissons) {
        if (permissons != null && permissons.length > 0) {
            setPermissionCallback = callback;
            setPermissions = permissons;
            Activity activity = PermissionsUtils.getActivity(act);
            if (activity != null) {
                openSetting(activity);
            }
        } else {
            if (callback != null) {
                callback.onPermissionsGranted(RequestCode.PERMISSION_SETTING);
            }
        }
    }

    public static void openSetting(Object act) {
        Activity activity = PermissionsUtils.getActivity(act);
        if (activity != null) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .setData(Uri.fromParts("package", activity.getPackageName(), null));
            activity.startActivityForResult(intent, RequestCode.PERMISSION_SETTING);
        }
    }

    public static void openSettingForContext(Context context) {
        if (context != null) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .setData(Uri.fromParts("package", context.getPackageName(), null));
            context.startActivity(intent);
        }
    }

    public static boolean isNoticePermissionOpen(Context context) {
        boolean isOpened = false;
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        if (manager != null) {
            isOpened = manager.areNotificationsEnabled();
        }
        return isOpened;
    }

    public static void openNoticeSetting(Context context) {
        openSettingForContext(context);
    }
}
