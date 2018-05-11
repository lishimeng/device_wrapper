package com.thingple.tagservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.thingple.tag.wrapper.R;
import com.thingple.tagservice.device.context.DeviceContext;


/**
 * Device Service
 */
public class IDeviceService extends Service {

    private IBinder binder;

    private NotificationManager notificationManager;

    private int notifyId = 1;

    public IDeviceService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NotificationChannel.DEFAULT_CHANNEL_ID);
            Notification notification = builder.setWhen(System.currentTimeMillis())
                    .setContentTitle("Tag")
                    .setContentText("Inventory正在执行")
                    .setTicker("Inventory正在执行")
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setOngoing(true)
                    .setSmallIcon(R.drawable.ic_notify_reader)
                    .setAutoCancel(true)

                    .build();
            notification.flags |= Notification.FLAG_NO_CLEAR;

            this.notificationManager.notify(notifyId, notification);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        if (this.binder == null) {
            this.binder = new DeviceContext(this);
        }
        return this.binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (notificationManager != null) {
            notificationManager.cancel(notifyId);
        }
    }
}
