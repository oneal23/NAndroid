package com.na.base.ui.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.na.base.AppManager;
import com.na.utils.SbUtil;
import com.na.utils.permission.PermissionsChecker;

/**
 * Created by oneal23 on 2018/6/26.
 */
public class BaseActivity extends AppCompatActivity {

    protected void hideActionBar() {
        getSupportActionBar().hide();
    }

    protected void showActionBar() {
        getSupportActionBar().show();
    }

    protected void setStatusBarColor(@ColorInt int color){
        SbUtil.setColor(this, color, 0);
    }

    protected void setStatusBarColorResId(int colorResId) {
        if (colorResId > 0){
            int color = getResources().getColor(colorResId);
            setStatusBarColor(color);
        }
    }

    protected void setLightMode(){
        SbUtil.setLightMode(this);
    }

    protected void setDarkMode() {
        SbUtil.setDarkMode(this);
    }

    protected void setTransparent() {
//        StatusBarUtil.setTransparent(this);
        setTransparentDiff();
    }

    protected void setTransparentDiff() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    protected void clearTransparentDiff() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    protected boolean isFullSreen() {
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        hideActionBar();
        if (isFullSreen()){
            setTransparent();
        }
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().removeActivity(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsChecker.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PermissionsChecker.onActivityResult(this, requestCode, resultCode, data);
    }
}
