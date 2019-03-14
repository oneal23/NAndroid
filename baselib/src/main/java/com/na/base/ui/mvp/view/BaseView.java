package com.na.base.ui.mvp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by oneal23 on 2018/6/26.
 */
public abstract class BaseView implements IBaseView {

    public static interface EventListener{
        void onEvent(int event);
    }

    private EventListener mEventListener;

    protected View mRootView;

    protected abstract int getLayoutId();

    public void setEventListener(EventListener mEventListener) {
        this.mEventListener = mEventListener;
    }

    public EventListener getEventListener() {
        return mEventListener;
    }

    @Override
    public View getView() {
        return mRootView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
