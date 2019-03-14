package com.na.base.ui.mvp.databind;


import com.na.base.ui.mvp.model.IBaseModel;
import com.na.base.ui.mvp.view.IBaseView;

/**
 * Created by oneal23 on 2018/6/26.
 */
public interface IDataBinder<V extends IBaseView, M extends IBaseModel> {
    void viewBindModel(V viewDelegate, M data);
}
