package com.koenschouten.notificationcreator.receivers;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

public class NotificationBroadCastReceiver extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notification-id" ;
    public static String NOTIFICATION = "notification" ;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("onNotificationReceived", "Notification received");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Notification notification = intent.getParcelableExtra( NOTIFICATION );
        Log.d("onNotificationReceived", "notification: " + notification.toString());

        int id = intent.getIntExtra( NOTIFICATION_ID , 0 ) ;

        notificationManager.notify(id , notification) ;
    }
}
