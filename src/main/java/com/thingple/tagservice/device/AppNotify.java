package com.thingple.tagservice.device;

import android.content.Context;
import android.os.Vibrator;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 *
 * Created by lism on 2017/8/15.
 */
class AppNotify {

    private Context context;

    AppNotify(Context context) {
        this.context = context;
        init();
    }

    private void init() {
    }

    void start() {
        long[] pattern = {1000, 400, 1000, 400};
        Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(pattern, 0);
        }
    }

    void destroy() {
        Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.cancel();
        }
    }
}
