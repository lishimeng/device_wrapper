package com.thingple.tagservice;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.thingple.tagservice.device.IDevice;


/**
 *
 * Created by lism on 2017/7/28.
 */

public class ReadCardHandler extends Handler {

    private ReadCardListener listener;

    public ReadCardHandler(ReadCardListener listener) {
        this.listener = listener;
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == IDevice.MEG) {

            Bundle bundle = msg.getData();
            String tid = bundle.getString("TID");
            String epc = bundle.getString("EPC");
            if (this.listener != null) {
                listener.onReadCard(tid, epc);
            }
        }
    }
}
