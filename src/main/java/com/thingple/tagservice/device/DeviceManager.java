package com.thingple.tagservice.device;

import android.content.Context;
import android.util.Log;

import com.thingple.tagservice.device.vendor.AbstractDevice;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * RFID设备管理器
 * Created by lism on 2017/8/9.
 */

public class DeviceManager {

    private static DeviceManager ins;

    private static Object lock = new Object();

    private Context context;

    private static Map<String, Class<? extends AbstractDevice>> clazzMap = new HashMap<>();
    private static Map<String, IDevice> deviceMap = new HashMap<>();

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

    public IDevice getDevice(String category) {
        IDevice device = getCachedDevice(category);
        if (device == null) {
            synchronized (lock) {
                initDevice(category);
            }
        }
        return device;
    }

    private IDevice getCachedDevice(String category) {
        return deviceMap.get(category);
    }

    private void initDevice(String category) {
        IDevice device = getCachedDevice(category);
        if (device == null) {
            device = createDevice(category);
            deviceMap.put(category, device);
        }
    }

    private IDevice createDevice(String category) {
        IDevice device = null;
        try {
            Class<? extends AbstractDevice> clazz = clazzMap.get(category);
            Constructor<? extends AbstractDevice> constructor = clazz.getConstructor(Context.class);
            device = constructor.newInstance(context);
        } catch (Exception e) {
            Log.e(getClass().getName() + "#createDevice", "Failed to create device instance", e);
        }

        return device;
    }

    private void destroySelf() {

        this.context = null;
        for (String category : deviceMap.keySet()) {
            IDevice device = deviceMap.get(category);
            deviceMap.remove(category);
            closeDevice(device);
        }
    }

    private void closeDevice(IDevice device) {
        if (device != null) {
            device.getMonitor().cancel();
            device.closeDevice();
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

    public static void registerDevice(String category, AbstractDevice device) {
        deviceMap.put(category, device);
    }

    public static void registerDevice(String category, Class<? extends AbstractDevice> clazz) {
        clazzMap.put(category, clazz);
    }
}
