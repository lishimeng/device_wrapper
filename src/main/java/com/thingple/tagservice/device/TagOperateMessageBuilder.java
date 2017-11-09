package com.thingple.tagservice.device;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * TagMessageBuilder
 * Created by lism on 2017/8/8.
 */

public class TagOperateMessageBuilder {

    private int code;

    private String message;

    private TagOperateMessageBuilder() {

    }

    public static TagOperateMessageBuilder newInstance() {
        return new TagOperateMessageBuilder();
    }

    public TagOperateMessageBuilder code(int code) {
        this.code = code;
        return this;
    }

    public TagOperateMessageBuilder message(String message) {
        this.message = message;
        return this;
    }

    public Bundle build() {

        Bundle bundle = new Bundle();
        bundle.putInt("code", code);
        bundle.putString("message", message);
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
