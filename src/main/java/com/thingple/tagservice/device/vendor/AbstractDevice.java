package com.thingple.tagservice.device.vendor;

import android.content.Context;

import com.thingple.tagservice.Common;
import com.thingple.tagservice.WriteCardListener;
import com.thingple.tagservice.device.IDevice;


/**
 * 设备
 * Created by lism on 2017/7/28.
 */
public abstract class AbstractDevice implements IDevice {

    protected Context context;

    private long lastVisit = -1;

    private static byte[] defaultPassword = new byte[4];
    static {
        Common.hexStr2Bytes("00000000", defaultPassword, 0, 4);
    }

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

    @Override
    public long lastVisit() {
        return lastVisit;
    }

    protected byte[] genPassword(String password) {

        byte[] pwd;
        if (password != null && password.length() >= 8) {
            pwd = new byte[4];
            Common.hexStr2Bytes(password, pwd, 0, 4);
        } else {
            pwd = defaultPassword;
        }
        return pwd;
    }
}
