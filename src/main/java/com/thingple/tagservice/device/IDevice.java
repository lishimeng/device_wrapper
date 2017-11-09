package com.thingple.tagservice.device;

import android.os.Handler;

import com.thingple.tagservice.WriteCardListener;
import com.thingple.tagservice.device.vendor.TagArea;

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
     * @return 0:打开
     */
    boolean isOpened();

    /**
     * 关闭设备
     */
    void closeDevice();

    long lastVisit();

    /**
     * 写卡
     * @param epc 当前epc
     * @param area 写卡的区域
     * @param data 写入数据
     * @param passwd 密码
     * @return true/false
     */
    void writeCard(String epc, TagArea area, String data, String passwd, WriteCardListener callback);

    /**
     * 写卡/默认epc
     * @param epc 当前epc
     * @param data 写入数据
     * @param passwd 密码
     * @return true/false
     */
    void writeCard(String epc, String data, String passwd, WriteCardListener callback);
}
