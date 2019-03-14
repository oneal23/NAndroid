package com.na.base.ui.mvp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by oneal23 on 2018/6/26.
 */
public interface IBaseView {

    View getView();

    View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    void onViewCreated();

    void onDestroy();

    void onResume();

    void onPause();

    void onStart();

    void onStop();
}
