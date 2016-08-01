package com.example.lcom67;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by lcom67 on 27/7/16.
 */

public class YourService extends Service
{
    AlarmReceiver alarm = new AlarmReceiver();
    public void onCreate()
    {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        alarm.SetAlarm(this);
        return START_STICKY;
    }

    @Override
   public void onStart(Intent intent, int startId) {
//        alarm.SetAlarm(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
