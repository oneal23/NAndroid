package com.na.base.ui.base;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.na.utils.permission.PermissionsChecker;

/**
 * Created by oneal23 on 2018/6/26.
 */
public class BaseFragment extends Fragment {

    protected void hideActionBar() {
        getBaseActivity().hideActionBar();
    }

    protected void showActionBar() {
        getBaseActivity().showActionBar();
    }

    protected void setStatusBarColor(@ColorInt int color) {
        getBaseActivity().setStatusBarColor(color);
    }

    protected void setStatusBarColorResId(int colorResId) {
        getBaseActivity().setStatusBarColorResId(colorResId);
    }

    protected void setLightMode() {
        getBaseActivity().setLightMode();
    }

    protected void setDarkMode() {
        getBaseActivity().setDarkMode();
    }

    protected boolean isFullSreen() {
        return false;
    }

    protected BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (isFullSreen()) {
            hideActionBar();
            setStatusBarColor(Color.TRANSPARENT);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
