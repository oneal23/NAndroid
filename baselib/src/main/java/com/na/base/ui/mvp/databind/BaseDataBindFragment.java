package com.na.base.ui.mvp.databind;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.na.base.ui.mvp.model.IBaseModel;
import com.na.base.ui.mvp.presenter.BaseFragmentPresenter;
import com.na.base.ui.mvp.view.IBaseView;


/**
 * Created by oneal23 on 2018/6/26.
 */
public abstract class BaseDataBindFragment<V extends IBaseView> extends BaseFragmentPresenter<V> {

    protected IDataBinder mDataBinder;

    protected IDataBinder createDataBinder(){
        return null;
    }

    protected IDataBinder getDataBinder() {
        return mDataBinder;
    }

    protected void setDataBinder(IDataBinder binder) {
        mDataBinder = binder;
    }

    public <M extends IBaseModel> void onUpdateData(M data){
        if(getDataBinder() != null){
            getDataBinder().viewBindModel(getBaseView(), data);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDataBinder(createDataBinder());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(getDataBinder() != null){
            setDataBinder(null);
        }
    }
}
