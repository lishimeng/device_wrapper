package com.thingple.tagservice.device;

/**
 * 设备监控回调
 * Created by lism on 2017/8/9.
 */

public interface DeviceIdleListener {

    /**
     * 设备空闲超时
     */
    void onIdleTimeout();
}
