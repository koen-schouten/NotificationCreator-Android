package com.koenschouten.notificationcreator.viewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.koenschouten.notificationcreator.database.tables.NotificationDB;
import com.koenschouten.notificationcreator.repositories.NotificationRepository;

import java.util.List;
import java.util.Objects;

public class NotificationsViewModel extends AndroidViewModel {
    private final NotificationRepository notificationRepository;
    private final LiveData<List<NotificationDB>> allNotifications;

    public NotificationsViewModel(Application app){
        super(app);
        notificationRepository = new NotificationRepository(app);
        allNotifications = notificationRepository.getAllNotifications();
    }

    public LiveData<List<NotificationDB>> getAllNotifications() { return allNotifications; }
    public void insert(NotificationDB notificationDB) { notificationRepository.insert(notificationDB); }

    public NotificationDB getNotificationByID(long notificationID){
        return notificationRepository.getNotificationByID(notificationID);
    }

    public NotificationDB getNotificationByIndex(int index){
        return Objects.requireNonNull(allNotifications.getValue()).get(index);
    }

    public void update(NotificationDB notificationDB){
        notificationRepository.update(notificationDB);
    }

    public void setFlagToDelete(NotificationDB notificationDBToDelete){
        notificationDBToDelete.setFlagDeleted(true);
        notificationRepository.update(notificationDBToDelete);
    }

    public void removeFlagToDelete(NotificationDB notificationDBToDelete){
        notificationDBToDelete.setFlagDeleted(false);
        notificationRepository.update(notificationDBToDelete);
    }



}
