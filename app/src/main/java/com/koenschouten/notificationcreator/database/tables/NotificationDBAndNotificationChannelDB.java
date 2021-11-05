package com.koenschouten.notificationcreator.database.tables;


import androidx.room.Embedded;
import androidx.room.Relation;


public class NotificationDBAndNotificationChannelDB {
    @Embedded public NotificationDB NotificationDB;
    @Relation(
            parentColumn = "notification_channel_id",
            entityColumn = "notification_channel_id"
    )
    public NotificationChannelDB notificationChannelDB;

}

