package com.thingple.tagservice.device.context;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Binder;
import android.util.Log;

import com.thingple.tagservice.HeartBeatReciever;
import com.thingple.tagservice.device.AppNotify;
import com.thingple.tagservice.device.DeviceManager;
import com.thingple.tagservice.device.DeviceMonitor;
import com.thingple.tagservice.device.IDevice;


/**
 * Device控制器
 * Created by lism on 2017/7/28.
 */
public class AbstractDeviceContext extends Binder {

    static final String INVENTORY_PERMISSION = "com.thingple.tag.permission.inventory";
    static final String INVENTORY_ACTION = "com.thingple.tag.inventory";

    private static String INVENTORY_HEARTBEAT_PERMISSION = "com.thingple.tag.permission.heartbeat";
    private static String INVENTORY_HEARTBEAT_ACTION = "com.thingple.tag.heartbeat";

    private Context context;

    HeartBeatReciever heartBeatReciever;

    AppNotify notify;

    String category = null;

    AbstractDeviceContext(Context context) {
        this.context = context;

        notify = new AppNotify(context);

        Log.d("DeviceContext", "注册heartbeat监听");
        this.heartBeatReciever = new HeartBeatReciever();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(INVENTORY_HEARTBEAT_ACTION);
        context.registerReceiver(this.heartBeatReciever, intentFilter, INVENTORY_HEARTBEAT_PERMISSION, null);

    }

    public Context getContext() {
        return this.context;
    }

    IDevice getAvailableDevice() {
        IDevice device = DeviceManager.shareInstance().getDevice(category);
        if (device == null) {
            return null;
        }
        if (!device.isOpened()) {
            device.openDevice();
        }

        DeviceMonitor monitor = device.getMonitor();
        if (!monitor.isStarted()) {// 打开设备后同时开始监控设备空闲状态,及时关闭
            monitor.start();
        }
        return device;
    }

    /**
     * 释放资源
     */
    public void dispose() {
        Log.d("DeviceContext", "解除heartbeat绑定");
        HeartBeatReciever bhr = this.heartBeatReciever;
        this.heartBeatReciever = null;
        context.unregisterReceiver(bhr);

        DeviceManager.destroy();
    }

}
