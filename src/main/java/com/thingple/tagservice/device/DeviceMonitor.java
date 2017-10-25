package com.thingple.tagservice.device;

/**
 * 设备监控运行<br/>
 * 超时后关闭设备<br/>
 * Created by lism on 2017/8/9.
 */
public interface DeviceMonitor {
    /**
     * 设备空闲的超时时间
     */
    long TIMEOUT = 30;

    void start();

    boolean isStarted();

    void cancel();

    void setListener(DeviceIdleListener listener);
}
