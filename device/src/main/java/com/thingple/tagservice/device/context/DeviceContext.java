package com.thingple.tagservice.device.context;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.thingple.tagservice.ReadCardHandler;
import com.thingple.tagservice.ReadCardListener;
import com.thingple.tagservice.WriteCardListener;
import com.thingple.tagservice.device.DeviceManager;
import com.thingple.tagservice.device.IDevice;
import com.thingple.tagservice.device.vendor.AbstractDevice;
import com.thingple.tagservice.device.vendor.TagArea;


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
    public void inventoryStart(String filterExp, int power, String deviceCategory) {
        this.category = deviceCategory;
        IDevice device = getAvailableDevice();
        if (device == null) {
            return;
        }
        if(!"barcode".equals(this.category)){
            notify.start();
        }
        //notify.start();
        Handler inventoryHandler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == IDevice.MEG) {
                    Bundle bundle = msg.getData();
                    String tid = bundle.getString("TID");
                    String epc = bundle.getString("EPC");
                    String rssi = bundle.getString("RSSI");
                    onInventory(tid, epc, rssi);
                }
                return false;
            }
        });
        if (!device.isOpened()) {
            device.openDevice();
        }
        device.configPower(power);
        if (device instanceof AbstractDevice) {
            AbstractDevice abstractDevice = (AbstractDevice) device;
            abstractDevice.preInventory();
        }
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
                if (idle >= 1400) {
                    inventoryStop();
                } else {
                    // mark visit time
                    IDevice device = DeviceManager.shareInstance().getDevice(category);
                    if (device != null && device instanceof AbstractDevice) {
                        AbstractDevice abstractDevice = (AbstractDevice) device;
                        abstractDevice.markVisit();
                    }
                    handler.postDelayed(this, 300);
                }
            }
        }, 500);
    }

    public void inventoryOnce(ReadCardListener callback, String filterExp, int power, String deviceCategory) {

        this.category = deviceCategory;
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
        if(!"barcode".equals(this.category)){
            notify.destroy();
        }
        IDevice device = DeviceManager.shareInstance().getDevice(category);
        if (device != null) {
            device.stopInventory();
        }
    }

    public void writeCard(final WriteCardListener callback, String epcForSelect, String data, String password, int power, String deviceCategory) {

        this.category = deviceCategory;
        IDevice device = getAvailableDevice();
        if (device != null) {
            device.configPower(power);
            device.writeCard(epcForSelect, data, password, callback);
        }
    }

    /**
     * 读区域的值
     * @param callback 回调
     * @param filterExp select
     * @param bank 区域
     * @param begin 开始地址
     * @param word 字数
     * @param passwd 密码
     * @param power 功率
     * @param deviceCategory 类型
     */
    public void readArea(ReadCardListener callback, String filterExp, int bank, int begin, int word, String passwd, int power, String deviceCategory) {
        this.category = deviceCategory;
        IDevice device = getAvailableDevice();
        if (device == null) {
            return;
        }
        TagArea area = TagArea.getBank(bank);
        if (area == null) {
            return;
        }

        device.configPower(power);
        device.readCard(filterExp, area, begin, word, passwd, callback);
    }

    /**
     * 读到标签
     * @param tid tid
     * @param epc epc
     */
    private void onInventory(String tid, String epc, String rssi) {
        Intent intent = new Intent(INVENTORY_ACTION);

        if (tid != null) {
            intent.putExtra("TID", tid);
        }
        intent.putExtra("EPC", epc);
        intent.putExtra("RSSI",rssi);
        Log.d(getClass().getName() + "#onInventory", "发送广播Action:" + INVENTORY_ACTION + "\tPermission:" + INVENTORY_PERMISSION);
        getContext().sendBroadcast(intent, INVENTORY_PERMISSION);
    }

}
