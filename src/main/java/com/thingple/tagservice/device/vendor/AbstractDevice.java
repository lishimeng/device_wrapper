package com.thingple.tagservice.device.vendor;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.thingple.tagservice.Common;
import com.thingple.tagservice.WriteCardListener;
import com.thingple.tagservice.device.IDevice;
import com.thingple.tagservice.device.TagInfo;
import com.thingple.tagservice.device.TagMessageBuilder;

import java.util.HashMap;
import java.util.Map;


/**
 * 设备
 * Created by lism on 2017/7/28.
 */
public abstract class AbstractDevice implements IDevice {

    protected Context context;

    private long lastVisit = -1;

    private static byte[] defaultPassword = new byte[4];

    private Map<String, Long> map = null;

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

    public void markVisit() {
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

    public void preInventory() {
        Log.d("preInventory", "init inventory");
        map = new HashMap<>();
    }

    protected void onTag(final Handler handler, final TagInfo tagInfo) {

        long now = System.currentTimeMillis();
        if (map != null) {
            Long latestVisitTime = map.get(tagInfo.epc);
            if (latestVisitTime == null) {// 首次
                processTag(handler, tagInfo, now);
            } else {
                if ((now - latestVisitTime) > 500) {// 超过500millisecond
                    processTag(handler, tagInfo, now);
                }
            }
        } else {
            processTag(handler, tagInfo, now);
        }

    }

    private void processTag(final Handler handler, final TagInfo tagInfo, final long processTime) {
        if (map != null) {
            map.put(tagInfo.epc, processTime);
        }
        Log.d(getClass().getName() + "#processTag", "读到标签 tid:" + tagInfo.tid + "\tepc:" + tagInfo.epc);
        TagMessageBuilder.newInstance().tid(tagInfo.tid).epc(tagInfo.epc).rssi(tagInfo.rssi).build(handler);
    }
}
