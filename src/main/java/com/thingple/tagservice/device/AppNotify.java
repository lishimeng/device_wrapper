package com.thingple.tagservice.device;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

/**
 *
 * Created by lism on 2017/8/15.
 */

public class AppNotify {

    private static int notifyId = 0;

    private Context context;

    NotificationManager notificationManager;

    public AppNotify(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        notificationManager = (NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void start() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context);
        Notification notification = builder.setWhen(System.currentTimeMillis())
                .setContentTitle("Tag")
                .setContentText("Inventory正在执行")
                .setTicker("Inventory正在执行")
                .setDefaults(Notification.DEFAULT_SOUND)
                .setOngoing(true)
                .setAutoCancel(true)
                .build();
        notification.flags |= Notification.FLAG_INSISTENT;

        this.notificationManager.notify(notifyId, notification);

    }

    public void destroy() {
        notificationManager.cancel(notifyId);
    }
}
