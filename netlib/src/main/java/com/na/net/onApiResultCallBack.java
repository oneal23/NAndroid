package com.na.net;

import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;

public class onApiResultCallBack extends DisposableObserver<ResponseBody> {

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onNext(ResponseBody responseBody) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }

    protected void onCancel() {
        if (!this.isDisposed()) {
            this.dispose();
        }
    }
}
