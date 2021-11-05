package com.koenschouten.notificationcreator.database.tables;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NotificationChannelDBDao {

    @Query("SELECT * FROM NotificationChannelDB WHERE flag_deleted = 0 ORDER BY date_created ASC ")
    LiveData<List<NotificationChannelDB>> getAll();

    @Query("SELECT * FROM NotificationChannelDB WHERE flag_deleted = 0 ORDER BY date_created ASC ")
    List<NotificationChannelDB> getAllAsList();

    @Insert
    void insert(NotificationChannelDB notificationChannelDB);

    @Update()
    void update(NotificationChannelDB notificationChannelDB);

    @Insert
    void insertAll(NotificationChannelDB... notificationChannelDBs);

    @Delete
    void delete(NotificationChannelDB notificationChannelDB);

    @Query("SELECT * FROM NotificationChannelDB WHERE notification_channel_id = :notificationChannelID")
    NotificationChannelDB getById(long notificationChannelID);

    @Query("SELECT * FROM NotificationChannelDB WHERE title = :title" )
    NotificationChannelDB getByTitle(String title);

}
