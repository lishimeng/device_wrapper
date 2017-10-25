package com.thingple.tagservice.device;

import android.os.Handler;

/**
 * 设备定义
 * Created by lism on 2017/7/27.
 */

public interface IDevice {

    int MEG = 1;

    /**
     * 停止盘点
     */
    void stopInventory();

    /**
     * 开始盘点
     * @param handler 回调
     */
    void startInventory(final Handler handler, String filterExp);

    /**
     * 设置功率
     * @param n 值
     */
    void configPower(int n);

    /**
     * 打开设备
     */
    void openDevice();

    /**
     * 设备状态打开
     * @return
     */
    boolean isOpened();

    /**
     * 关闭设备
     */
    void closeDevice();

    long lastVisit();
}
