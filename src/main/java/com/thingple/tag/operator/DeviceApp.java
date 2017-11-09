package com.thingple.tag.operator;

import android.app.Application;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.thingple.tagservice.IDeviceService;
import com.thingple.tagservice.device.DeviceContext;
import com.thingple.tagservice.device.DeviceManager;


/**
 * Application
 * Created by lism on 2017/7/27.
 */

public class DeviceApp extends Application {

    public DeviceContext deviceContext;

    public ServiceConnection deviceServiceConn;

    @Override
    public void onCreate() {
        super.onCreate();

        startDevice();
        DeviceManager.init(this.getApplicationContext());

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        DeviceManager.destroy();
    }

    public void initConnDeviceService() {
        this.deviceServiceConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                deviceContext = (DeviceContext) service;
                Log.d("TagService", "Service 已经得到");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                deviceContext.dispose();
            }
        };
    }

    public void startDevice() {

        Log.d("tag", "启动Service");
        initConnDeviceService();
        Intent intent = new Intent(this, IDeviceService.class);
        this.bindService(intent, this.deviceServiceConn, Service.BIND_AUTO_CREATE);
        // cache exception
    }

    public DeviceContext getDeviceContext() {
        Log.d("tag", "获取device context");
        if (this.deviceContext == null) {
            Log.d("tag", "device context是null");
        }
        return this.deviceContext;
    }

}
