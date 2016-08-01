package com.example.lcom67;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.example.lcom67.demoapp.R;
import com.example.lcom67.demoapp.RecyclerViewActivity;
import com.example.lcom67.demoapp.SetReminderSongActivity;

import java.util.Calendar;

/**
 * Created by lcom67 on 27/7/16.
 */

public class AlarmReceiver extends BroadcastReceiver
{
    private static final int MY_NOTIFICATION_ID = 0;
//    NotificationManager notificationManager;
//    Notification myNotification;
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";



    @Override
    public void onReceive(Context context, Intent intent)
    {
        Toast.makeText(context, "Time is set", Toast.LENGTH_LONG).show();

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();

//        Calendar c = Calendar.getInstance();
//        Intent myIntent = new Intent(context, RecyclerViewActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(
//                context,
//                0,
//                myIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//        myNotification = new NotificationCompat.Builder(context)
//                .setContentTitle("Alarm..")
//                .setWhen(c.getTimeInMillis())
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true)
//                .setSmallIcon(R.drawable.logo)
//                .build();
//        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(MY_NOTIFICATION_ID, myNotification);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notificationuri = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notificationuri);
    }
}
