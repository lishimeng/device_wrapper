package com.thingple.tagservice.device;

import android.content.Context;

import com.thingple.tagservice.device.vendor.AbstractDevice;

import java.lang.reflect.Constructor;

/**
 * RFID设备管理器
 * Created by lism on 2017/8/9.
 */

public class DeviceManager {

    private static DeviceManager ins;

    private static Object lock = new Object();

    private Context context;

    private IDevice device;

    public static Class<? extends AbstractDevice> clazz;

    public static void init(Context context) {
        if (ins == null) {
            ins = new DeviceManager(context);
        }
    }

    public static DeviceManager shareInstance() {
        return ins;
    }

    private DeviceManager(Context context) {
        this.context = context;
    }

    public IDevice getDevice() {
        if (device == null) {
            synchronized (lock) {
                initDevice();
            }
        }
        return device;
    }

    public void initDevice() {
        if (this.device == null) {
            device = createDevice();
        }
    }

    private IDevice createDevice() {
        try {
            Constructor<? extends AbstractDevice> constructor = clazz.getConstructor(Context.class);
            device = constructor.newInstance(context);
        } catch (Exception e) {

        }

        return null;
    }

    public void destroySelf() {

        this.context = null;
        closeDevice();
    }

    void closeDevice() {
        if (device != null) {
            IDevice d = device;
            device = null;
            d.closeDevice();
        }
    }

    /**
     * 安全释放DeviceManager
     */
    public static void destroy() {
        lock = null;
        if (ins != null) {
            DeviceManager deviceManager = ins;
            ins = null;
            deviceManager.destroySelf();
        }
    }
}
