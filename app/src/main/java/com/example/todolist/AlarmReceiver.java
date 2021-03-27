package com.example.todolist;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class AlarmReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent){
        final String ANDROID_CHANNEL_ID = "com.chikeandroid.tutsplustalerts.ANDROID";
        int notificationID = intent.getIntExtra("notificationID",0);
        String message = intent.getStringExtra("todo");

        Intent mainIntent = new Intent(context, ProfileActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);

        NotificationManager myNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification builder = new Notification.Builder(context)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("I'ts time")
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setChannelId(ANDROID_CHANNEL_ID)
                .build();

        myNotificationManager.notify(notificationID, builder);
    }
}
