package com.na.base.ui.mvp.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.na.base.ui.base.BaseActivity;
import com.na.base.ui.mvp.view.IBaseView;

/**
 * Created by oneal23 on 2018/6/26.
 */
public abstract class BaseActivityPresenter<V extends IBaseView> extends BaseActivity implements IBasePresenter {
    protected V mView;

    public void createBaseView() {
        if (mView == null) {
            try {
                mView = (V) getBaseViewClass().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("create IBaseView error");
            }
        }
    }

    @Override
    public V getBaseView() {
        return mView;
    }

    public void setView(V mView) {
        this.mView = mView;
    }

    protected void bindEventListener(){

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createBaseView();
        View view = getBaseView().onCreateView(getLayoutInflater(), null, savedInstanceState);
        setContentView(view);
        getBaseView().onViewCreated();
        bindEventListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getBaseView() != null){
            getBaseView().onDestroy();
        }
        setView(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getBaseView() != null){
            getBaseView().onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getBaseView() != null){
            getBaseView().onPause();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getBaseView() != null){
            getBaseView().onStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (getBaseView() != null){
            getBaseView().onStop();
        }
    }
}
