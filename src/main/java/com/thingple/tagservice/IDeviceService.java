package com.thingple.tagservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.thingple.tagservice.device.DeviceContext;


/**
 * Device Service
 */
public class IDeviceService extends Service {

    private IBinder binder;

    public IDeviceService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        if (this.binder == null) {
            this.binder = new DeviceContext(this);
        }
        return this.binder;
    }
}
