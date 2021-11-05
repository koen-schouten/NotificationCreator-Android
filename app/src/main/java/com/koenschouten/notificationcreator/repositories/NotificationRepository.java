package com.koenschouten.notificationcreator.repositories;


import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.koenschouten.notificationcreator.NotificationCreatorManager;
import com.koenschouten.notificationcreator.database.AppDatabase;
import com.koenschouten.notificationcreator.database.tables.NotificationDB;
import com.koenschouten.notificationcreator.database.tables.NotificationDBDao;

import java.util.Date;
import java.util.List;

//Used a repository as advised by https://developer.android.com/codelabs/android-room-with-a-view#8
public class NotificationRepository {
    private final NotificationDBDao notificationDBDao;
    private final LiveData<List<NotificationDB>> allNotifications;
    private final Context context;

    public NotificationRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        notificationDBDao = appDatabase.notificationDBDao();
        allNotifications = notificationDBDao.getAll();
        context = application.getApplicationContext();
    }

    public LiveData<List<NotificationDB>> getAllNotifications() {
        return allNotifications;
    }

    public void insert(NotificationDB notificationDB) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            //Insert notification into database. It automatically gets an ID after insertion
            long newNotificationID = notificationDBDao.insert(notificationDB);
            //Set the notificationID to the new ID.
            notificationDB.notificationID = newNotificationID;

            //Create a real notification
            NotificationCreatorManager notificationCreatorManager = NotificationCreatorManager.getInstance();
            notificationCreatorManager.scheduleNotification(notificationDB, context);
        });
    }

    public void update(NotificationDB notificationDB) {
        NotificationCreatorManager notificationCreatorManager = NotificationCreatorManager.getInstance();

        AppDatabase.databaseWriteExecutor.execute(() ->{
            //Get old notification from Database
            NotificationDB oldNotification = notificationDBDao.getById(notificationDB.notificationID);
            //unschedule the notification
            notificationCreatorManager.unScheduleNotification(oldNotification, context);

            //if the new notification is not deleted and is in the future, then schedule a new notification
            if(!notificationDB.getFlagDeleted() && notificationDB.notificationDate.after(new Date(System.currentTimeMillis()))){
                notificationCreatorManager.scheduleNotification(notificationDB, context);
            }

            //update the new notification in database
            notificationDBDao.update(notificationDB);

        });
    }

    public NotificationDB getNotificationByID(long notificationID) {
        return notificationDBDao.getById(notificationID);
    }
}
