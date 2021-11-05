package com.koenschouten.notificationcreator.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;


import com.koenschouten.notificationcreator.NotificationCreatorManager;
import com.koenschouten.notificationcreator.database.AppDatabase;
import com.koenschouten.notificationcreator.database.tables.NotificationDB;
import com.koenschouten.notificationcreator.database.tables.NotificationDBDao;

import java.util.Date;
import java.util.List;


public class OnBootJobService extends JobService
{
    NotificationCreatorManager notificationCreatorManager = NotificationCreatorManager.getInstance();


    @Override
    public boolean onStartJob(JobParameters params) {
        getNotificationsFromDataBase();

        return false;
    }

    private void scheduleNotification(List<NotificationDB> notifications){
        for(NotificationDB notification: notifications) {
            notificationCreatorManager.scheduleNotification(notification, getApplicationContext());
        }
    }

    private void getNotificationsFromDataBase(){
        AppDatabase appDatabase = AppDatabase.getInstance(this);
        appDatabase.getQueryExecutor().execute(()->{
            NotificationDBDao notificationDBDao = appDatabase.notificationDBDao();
            List<NotificationDB> pendingNotifications = notificationDBDao.getNotificationsFrom(new Date(System.currentTimeMillis()));
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(()->scheduleNotification(pendingNotifications));
        });
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}