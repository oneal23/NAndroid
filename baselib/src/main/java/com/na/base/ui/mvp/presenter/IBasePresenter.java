package com.na.base.ui.mvp.presenter;


import com.na.base.ui.mvp.view.IBaseView;

/**
 * Created by oneal23 on 2018/6/26.
 */
public interface IBasePresenter<V extends IBaseView> {
    Class<V> getBaseViewClass();
    V getBaseView();
}
