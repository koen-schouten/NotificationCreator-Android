package com.koenschouten.notificationcreator;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.koenschouten.notificationcreator.database.tables.NotificationChannelDB;
import com.koenschouten.notificationcreator.database.tables.NotificationDB;
import com.koenschouten.notificationcreator.receivers.NotificationBroadCastReceiver;

import java.util.Date;


public class NotificationCreatorManager {
    private static NotificationCreatorManager instance;

    private PendingIntent createPendingIntentFromNotification(NotificationDB notificationDB, Context context){
        context = context.getApplicationContext();
        Notification notification = getNotification(notificationDB, context);
        Intent notificationIntent = new Intent(context, NotificationBroadCastReceiver. class) ;
        notificationIntent.putExtra(NotificationBroadCastReceiver.NOTIFICATION_ID , (int)notificationDB.notificationID) ;
        notificationIntent.putExtra(NotificationBroadCastReceiver.NOTIFICATION , notification) ;
        int requestCode = (int) notificationDB.notificationID;
        return PendingIntent.getBroadcast ( context, requestCode , notificationIntent , PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void scheduleNotification (NotificationDB notificationDB, Context context) {
        //Only schedule a notification if it is set in the future
        if(notificationDB.notificationDate.getTime() > System.currentTimeMillis()) {
            PendingIntent pendingIntent = createPendingIntentFromNotification(notificationDB, context);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, notificationDB.notificationDate.getTime(), pendingIntent);
        }
    }

    public void unScheduleNotification(NotificationDB notificationDB, Context context){
        PendingIntent pendingIntent = createPendingIntentFromNotification(notificationDB, context);

        //Cancel alarm of pending notificationIntent
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        //Delete the actual notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel((int) notificationDB.notificationID);
    }

    private Notification getNotification(NotificationDB notificationDB, Context context){
        context = context.getApplicationContext();
        //Create a notification object
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, String.valueOf(notificationDB.notificationChannelId))
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle(notificationDB.contentTitle)
                .setContentText(notificationDB.contentMessage)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setWhen(notificationDB.notificationDate.getTime())
                .setColor(Color.argb(122,0,255,0))
                .setColorized(true);
        return builder.build();
    }

    public void createNotificationChannel(NotificationChannelDB notificationChannelDB, Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = notificationChannelDB.title;
            String description = notificationChannelDB.description;
            String channelID = String.valueOf(notificationChannelDB.notificationChannelID);
            int importance = notificationChannelDB.importance.getImportance();

            NotificationChannel channel = new NotificationChannel(channelID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            android.app.NotificationManager notificationManager = context.getSystemService(android.app.NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static NotificationCreatorManager getInstance() {
        if (instance == null) {
            instance = new NotificationCreatorManager();
        }
        return instance;
    }
}
