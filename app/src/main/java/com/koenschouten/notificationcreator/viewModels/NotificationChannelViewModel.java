package com.koenschouten.notificationcreator.viewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.koenschouten.notificationcreator.database.tables.NotificationChannelDB;
import com.koenschouten.notificationcreator.repositories.NotificationChannelRepository;

import java.util.List;
import java.util.Objects;

public class NotificationChannelViewModel extends AndroidViewModel {
    private final NotificationChannelRepository notificationChannelRepository;
    private final LiveData<List<NotificationChannelDB>> allNotificationChannels;

    public NotificationChannelViewModel(Application app){
        super(app);
        notificationChannelRepository = new NotificationChannelRepository(app);
        allNotificationChannels = notificationChannelRepository.getAllNotificationChannels();
    }

    public LiveData<List<NotificationChannelDB>> getAllNotificationChannels() { return allNotificationChannels; }


    public void insert(NotificationChannelDB notificationChannel) { notificationChannelRepository.insert(notificationChannel); }

    public NotificationChannelDB getNotificationChannelByID(int notificationChannelID){
        return notificationChannelRepository.getNotificationChannelByID(notificationChannelID);
    }

    public NotificationChannelDB getNotificationChannelByIndex(int index){
        return Objects.requireNonNull(allNotificationChannels.getValue()).get(index);
    }

    public void update(NotificationChannelDB notificationChannelDB){
        notificationChannelRepository.update(notificationChannelDB);
    }

    public NotificationChannelDB getNotificationChannelByTitle(String title){
        return notificationChannelRepository.getNotificationChannelByTitle(title);
    }

    public void setFlagToDelete(NotificationChannelDB notificationDBToDelete){
        notificationDBToDelete.setFlagDeleted(true);
        notificationChannelRepository.update(notificationDBToDelete);
    }

    public void removeFlagToDelete(NotificationChannelDB notificationDBToDelete){
        notificationDBToDelete.setFlagDeleted(false);
        notificationChannelRepository.update(notificationDBToDelete);
    }
}
