package com.thingple.tagservice.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.thingple.tag.operator.DeviceApp;
import com.thingple.tagservice.device.DeviceContext;


/**
 * view基础类
 * Created by lism on 2017/7/28.
 */

public class BaseActivity extends AppCompatActivity {

    private static String TAG = "Reader";

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
                    int p = bundle.getInt("power");
                    if (p > 0) {
                        power = p;
                        Log.d(TAG + "#getPower", "指定本次操作功率值:" + power);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG + "#getPower", "获取功率参数失败", e);
        }

        return power;
    }

    protected DeviceContext getDeviceContext(final DeviceApp app) {
        DeviceContext deviceContext = null;
        if (app != null) {
            deviceContext = app.getDeviceContext();
        }
        return deviceContext;
    }
}
