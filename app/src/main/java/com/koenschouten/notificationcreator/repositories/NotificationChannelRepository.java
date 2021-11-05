package com.koenschouten.notificationcreator.repositories;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.koenschouten.notificationcreator.NotificationCreatorManager;
import com.koenschouten.notificationcreator.database.AppDatabase;
import com.koenschouten.notificationcreator.database.tables.NotificationChannelDB;
import com.koenschouten.notificationcreator.database.tables.NotificationChannelDBDao;

import java.util.List;

public class NotificationChannelRepository {
    NotificationChannelDBDao notificationChannelDBDao;
    LiveData<List<NotificationChannelDB>> allNotificationChannels;
    private Context context;

    public NotificationChannelRepository(Application application){
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        this.notificationChannelDBDao = appDatabase.notificationChannelDBDao();
        this.allNotificationChannels = notificationChannelDBDao.getAll();
        this.context = application.getApplicationContext();
    }

    public LiveData<List<NotificationChannelDB>> getAllNotificationChannels() {
        return allNotificationChannels;
    }

    public void insert(NotificationChannelDB notificationChannelDB){
        //Insert channel into database
        AppDatabase.databaseWriteExecutor.execute(() -> {
            notificationChannelDBDao.insert(notificationChannelDB);
            NotificationChannelDB channel = getNotificationChannelByTitle(notificationChannelDB.title);
            NotificationCreatorManager notificationCreatorManager = NotificationCreatorManager.getInstance();
            notificationCreatorManager.createNotificationChannel(channel, context);
        });

        //create a real notificationChannel

    }

    public void update(NotificationChannelDB notificationChannelDB){
        NotificationChannelDB oldNotificationChannel = getNotificationChannelByID(notificationChannelDB.notificationChannelID);
        //todo get all notifications that use this channel

        //if the notificationChannel gets deleted, unschedule all notifications that use this channel
        if(notificationChannelDB.getFlagDeleted() && !oldNotificationChannel.getFlagDeleted()){
            //todo unschedule all notifications
        }

        //If the notifcationChannel gets undeleted, reschedule all notifications that use this channel
        if(!notificationChannelDB.getFlagDeleted() && oldNotificationChannel.getFlagDeleted()){
            //todo reschedule all notifications
        }

        //todo delete old real notification channel
        //todo create new real notification channel if necessary
        //todo update  notification to use this new  channel or delete them
        AppDatabase.databaseWriteExecutor.execute(() -> notificationChannelDBDao.update(notificationChannelDB));
    }


    public NotificationChannelDB getNotificationChannelByID(long notificationChannelID) {
        return notificationChannelDBDao.getById(notificationChannelID);
    }

    public NotificationChannelDB getNotificationChannelByTitle(String title) {
        return notificationChannelDBDao.getByTitle(title);
    }
}
