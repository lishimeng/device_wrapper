package com.thingple.tagservice.device;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * TagMessageBuilder
 * Created by lism on 2017/8/8.
 */

public class TagMessageBuilder {

    private String tid;

    private String epc;

    private String rssi;

    private TagMessageBuilder() {

    }

    public static TagMessageBuilder newInstance() {
        return new TagMessageBuilder();
    }

    public TagMessageBuilder tid(String tid) {
        this.tid = tid;
        return this;
    }

    public TagMessageBuilder epc(String epc) {
        this.epc = epc;
        return this;
    }

    public TagMessageBuilder rssi(String rssi) {
        this.rssi = rssi;
        return this;
    }

    public Bundle build() {

        Bundle bundle = new Bundle();
        bundle.putString("TID", tid);
        bundle.putString("EPC", epc);
        bundle.putString("RSSI", rssi);
        return bundle;
    }

    public void build(Handler handler) {
        Bundle bundle = build();
        Message message = new Message();
        message.what = IDevice.MEG;
        message.setData(bundle);
        handler.sendMessage(message);
    }
}
