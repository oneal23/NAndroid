package com.na.app.ui.login;

import android.os.Bundle;

import com.na.app.R;
import com.na.base.ui.mvp.databind.BaseDataBindActivity;

public class LoginActivity extends BaseDataBindActivity<LoginView> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public Class getBaseViewClass() {
        return LoginView.class;
    }
}
