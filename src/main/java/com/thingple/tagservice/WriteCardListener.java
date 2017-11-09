package com.thingple.tagservice;

/**
 * 读取操作的回调
 *
 * Created by lism on 2017/7/28.
 */
public interface WriteCardListener {

    void afterWriteCard(int code, String message);
}
