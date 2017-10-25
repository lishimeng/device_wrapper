package com.thingple.tagservice;

/**
 * 读取操作的回调
 *
 * Created by lism on 2017/7/28.
 */
public interface ReadCardListener {
    void onReadCard(String tid, String epc);
}
