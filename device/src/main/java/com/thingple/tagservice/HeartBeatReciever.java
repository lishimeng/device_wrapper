package com.thingple.tagservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 后台心跳监听
 * Created by lism on 2017/7/31.
 */

public class HeartBeatReciever extends BroadcastReceiver {

    public long lastTime;
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(getClass().getName() + "#onReceive", "收到Heartbeat");
        lastTime = System.currentTimeMillis();
    }

    public void reset(long time) {
        this.lastTime = time;
    }
}
