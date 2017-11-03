package com.thingple.tagservice.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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

    protected int getPower() {
        int power = 23;// default power
        try {
            Intent intent = getIntent();
            if (intent != null) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    power = bundle.getInt("power");
                }
            }
        } catch (Exception e) {
            Log.e(getClass().getName() + "#getPower", "获取功率参数失败", e);
        }

        return power;
    }
}
