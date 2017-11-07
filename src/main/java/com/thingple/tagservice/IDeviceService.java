package com.thingple.tagservice;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.thingple.tagservice.device.DeviceContext;


/**
 * Device Service
 */
public class IDeviceService extends Service {

    private IBinder binder;

    public IDeviceService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (mNotificationManager != null) {

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    this);
            mBuilder.setContentTitle(/*getString(R.string.notification_title)*/ "TITLE");
            mBuilder.setContentText(/*getString(R.string.notification_content)*/ "CONTENT");
            //mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            mNotificationManager.notify(1, mBuilder.build());
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
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }
}
