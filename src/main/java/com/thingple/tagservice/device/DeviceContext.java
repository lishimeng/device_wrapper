package com.thingple.tagservice.device;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.thingple.tagservice.ReadCardHandler;
import com.thingple.tagservice.ReadCardListener;

/**
 * Device控制器
 * Created by lism on 2017/7/28.
 */
public class DeviceContext extends AbstractDeviceContext {

    public DeviceContext(Context context) {
        super(context);
    }

    /**
     * 开始盘点操作
     */
    public void inventoryStart(String filterExp, int power) {
        IDevice device = getAvailableDevice();
        if (device == null) {
            return;
        }

        notify.start();

        Handler inventoryHandler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == IDevice.MEG) {

                    Bundle bundle = msg.getData();
                    String tid = bundle.getString("TID");
                    String epc = bundle.getString("EPC");
                    onInventory(tid, epc);
                }
                return false;
            }
        });
        if (!device.isOpened()) {
            device.openDevice();
        }
        device.configPower(power);
        device.startInventory(inventoryHandler, filterExp);
        inventoryListen();
    }

    /**
     * 根据心跳释放Inventory状态的设备<br/>
     * 调用方停止后不再更新心跳时间
     */
    private void inventoryListen() {
        this.heartBeatReciever.reset(System.currentTimeMillis());
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();
                long lastTime = heartBeatReciever.lastTime;
                long idle = now - lastTime;
                if (idle >= 1000) {
                    inventoryStop();
                } else {
                    handler.postDelayed(this, 300);
                }
            }
        }, 500);
    }

    public void inventoryOnce(ReadCardListener callback, String filterExp, int power) {

        IDevice device = getAvailableDevice();
        if (device == null) {
            return;
        }

        device.configPower(power);
        device.startInventory(new ReadCardHandler((callback)), filterExp);
    }

    /**
     * 主动调用停止inventory
     */
    public void inventoryStop() {
        notify.destroy();
        IDevice device = DeviceManager.shareInstance().getDevice();
        if (device != null) {
            device.stopInventory();
        }
    }

    /**
     * 读到标签
     * @param tid tid
     * @param epc epc
     */
    private void onInventory(String tid, String epc) {
        Intent intent = new Intent(INVENTORY_ACTION);

        if (tid != null) {
            intent.putExtra("TID", tid);
        }
        intent.putExtra("EPC", epc);
        Log.d(getClass().getName() + "#onInventory", "发送广播Action:" + INVENTORY_ACTION + "\tPermission:" + INVENTORY_PERMISSION);
        getContext().sendBroadcast(intent, INVENTORY_PERMISSION);
    }

}
