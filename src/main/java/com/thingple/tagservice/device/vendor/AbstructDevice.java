package com.thingple.tagservice.device.vendor;

import android.content.Context;

import com.thingple.tagservice.device.IDevice;


/**
 * 设备
 * Created by lism on 2017/7/28.
 */
public abstract class AbstructDevice implements IDevice {

    protected Context context;

    private boolean inInventory = false;
    private boolean opened = false;

    private long lastVisit = -1;

    public AbstructDevice(Context context) {
        lastVisit = System.currentTimeMillis();
        this.context = context.getApplicationContext();
    }

}
