package com.koenschouten.notificationcreator.database.tables;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface NotificationDBDao {
    @Query("SELECT * FROM NotificationDB WHERE flag_deleted = 0 ORDER BY notification_date DESC ")
    LiveData<List<NotificationDB>> getAll();

    @Query("SELECT * FROM NotificationDB WHERE flag_deleted = 0 AND notification_date > :fromDate ")
    List<NotificationDB> getNotificationsFrom(Date fromDate);

    @Insert
    long insert(NotificationDB notificationsDB);

    @Insert
    void insertAll(NotificationDB... notificationDBS);

    @Delete
    void delete(NotificationDB notificationDB);

    @Update()
    void update(NotificationDB notificationDB);

    @Query("DELETE FROM NotificationDB")
    void deleteAll();

    @Query("SELECT * FROM NotificationDB WHERE notification_id = :notificationID")
    NotificationDB getById(long notificationID);

    @Transaction
    @Query("SELECT * FROM NotificationChannelDB LEFT OUTER JOIN NotificationDB on NotificationDB.notification_channel_id = NotificationChannelDB.notification_channel_id")
    List<NotificationDBAndNotificationChannelDB> getNotificationAndChannel();
}
