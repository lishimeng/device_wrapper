package com.thingple.tagservice.device.impl;

import android.os.Handler;
import android.util.Log;

import com.thingple.tagservice.device.DeviceIdleListener;
import com.thingple.tagservice.device.DeviceManager;
import com.thingple.tagservice.device.DeviceMonitor;
import com.thingple.tagservice.device.IDevice;


public class DeviceMonitorImpl implements DeviceMonitor {

    private Handler handler = new Handler();

    private DeviceIdleListener listener;

    private boolean started = false;

    private boolean isCanceled = false;

    /**
     * 经过一段时间不使用后,关闭设备
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isCanceled) {
                started = false;
                return;
            }
            IDevice device = DeviceManager.shareInstance().device;
            if (device == null) {
                Log.d(getClass().getName() + "", "设备监控器:设备已经关闭,停止监控");
                return;
            }
            if (isTimeout(device)) {
                if (listener != null) {
                    listener.onIdleTimeout();
                }
                started = false;
            } else {
                if (handler != null) {
                    handler.postDelayed(this, 2000);
                }
            }

        }

        private boolean isTimeout(IDevice device) {
            long idleTime = System.currentTimeMillis() - device.lastVisit();
            Log.d(getClass().getName() + "", "设备监控器:设备空闲时间:" + idleTime);
            return idleTime > TIMEOUT * 1000;
        }
    };

    public void start() {// 监控设备,超时后自动停止
        if (!started) {
            started = true;
            if (handler != null && runnable != null) {
                handler.postDelayed(runnable, 5000);
            }
        }
    }

    public boolean isStarted() {
        return this.started;
    }

    public void cancel() {
        this.isCanceled = false;
    }

    public void setListener(DeviceIdleListener listener) {
        this.listener = listener;
    }
}
