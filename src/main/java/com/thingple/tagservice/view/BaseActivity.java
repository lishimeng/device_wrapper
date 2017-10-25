package com.thingple.tagservice.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.thingple.tagservice.DeviceApp;

/**
 * view基础类
 * Created by lism on 2017/7/28.
 */

public class BaseActivity extends AppCompatActivity {

    private DeviceApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (DeviceApp) getApplication();
    }

    public DeviceApp getApp() {
        return app;
    }
}
