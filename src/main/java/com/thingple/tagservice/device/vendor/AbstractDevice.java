package com.thingple.tagservice.device.vendor;

import android.content.Context;

import com.thingple.tagservice.WriteCardListener;
import com.thingple.tagservice.device.IDevice;


/**
 * 设备
 * Created by lism on 2017/7/28.
 */
public abstract class AbstractDevice implements IDevice {

    protected Context context;

    private boolean inInventory = false;
    private boolean opened = false;

    protected long lastVisit = -1;

    public AbstractDevice(Context context) {
        lastVisit = System.currentTimeMillis();
        this.context = context.getApplicationContext();
    }

    @Override
    public void writeCard(String epc, String data, String passwd, WriteCardListener callback) {
        writeCard(epc, TagArea.EPC, data, passwd, callback);
    }

    protected void mardVisit() {
        this.lastVisit = System.currentTimeMillis();
    }
}
