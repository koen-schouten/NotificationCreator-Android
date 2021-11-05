package com.koenschouten.notificationcreator.database.tables;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity(
        foreignKeys = {@ForeignKey(
                entity = NotificationChannelDB.class,
                parentColumns = "notification_channel_id",
                childColumns = "notification_channel_id",
                onDelete = ForeignKey.CASCADE
        )}
)
public class NotificationDB {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name ="notification_id")
    @NonNull
    public long notificationID;


    @ColumnInfo(name = "notification_channel_id", index = true)
    public long notificationChannelId;

    @ColumnInfo(name = "content_title")
    public String contentTitle;

    @ColumnInfo(name = "content_message")
    public String contentMessage;

    @ColumnInfo(name = "notification_date")
    public Date notificationDate;

    @ColumnInfo(name = "flag_deleted", defaultValue = "false")
    private boolean flagDeleted;

    public boolean getFlagDeleted(){
        return flagDeleted;
    }

    public void setFlagDeleted(boolean flag){
        flagDeleted = flag;
    }

    public void update(@NonNull String contentTitle, @NonNull String contentMessage, @NonNull Date notificationDate, long notificationChannelId){
        this.notificationChannelId = notificationChannelId;
        this.contentTitle = contentTitle;
        this.contentMessage = contentMessage;
        this.notificationDate = notificationDate;
    }

    public void update(@NonNull String contentTitle, @NonNull String contentMessage, @NonNull Date notificationDate){
        this.contentTitle = contentTitle;
        this.contentMessage = contentMessage;
        this.notificationDate = notificationDate;
    }

    public NotificationDB(@NonNull String contentTitle, @NonNull String contentMessage, @NonNull Date notificationDate, long notificationChannelId){
        this.notificationChannelId = notificationChannelId;
        this.contentTitle = contentTitle;
        this.contentMessage = contentMessage;
        this.notificationDate = notificationDate;
    }

    @Ignore
    public NotificationDB(@NonNull String contentTitle, @NonNull String contentMessage, @NonNull Date notificationDate, long notificationChannelId, long notificationID){
        this.notificationID = notificationID;
        this.notificationChannelId = notificationChannelId;
        this.contentTitle = contentTitle;
        this.contentMessage = contentMessage;
        this.notificationDate = notificationDate;
    }


}
