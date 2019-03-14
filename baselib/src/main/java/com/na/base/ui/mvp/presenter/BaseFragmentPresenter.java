package com.na.base.ui.mvp.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.na.base.ui.base.BaseFragment;
import com.na.base.ui.mvp.view.IBaseView;

/**
 * Created by oneal23 on 2018/6/26.
 */
public abstract class BaseFragmentPresenter<V extends IBaseView> extends BaseFragment implements IBasePresenter {

    protected V mView;

    public void createView() {
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        createView();
        View view = getBaseView().onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getBaseView() != null){
            getBaseView().onDestroy();
        }
        setView(null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getBaseView() != null){
            getBaseView().onViewCreated();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getBaseView() != null){
            getBaseView().onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getBaseView() != null){
            getBaseView().onPause();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getBaseView() != null){
            getBaseView().onStart();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getBaseView() != null){
            getBaseView().onStop();
        }
    }
}
