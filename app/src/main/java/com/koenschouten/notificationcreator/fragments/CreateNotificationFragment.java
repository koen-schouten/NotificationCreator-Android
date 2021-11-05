package com.koenschouten.notificationcreator.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.Navigation;

import com.koenschouten.notificationcreator.NotificationCreatorManager;
import com.koenschouten.notificationcreator.R;
import com.koenschouten.notificationcreator.database.AppDatabase;
import com.koenschouten.notificationcreator.database.tables.NotificationChannelDB;
import com.koenschouten.notificationcreator.database.tables.NotificationChannelDBDao;
import com.koenschouten.notificationcreator.database.tables.NotificationDB;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CreateNotificationFragment extends BaseEditNotificationFragment {

    public CreateNotificationFragment() {
        // Required empty public constructor
    }

    @Override
    protected void submitForm(){
        //Get the selected Channel
        NotificationChannelDB channel = (NotificationChannelDB) formChannelSpinner.getSelectedItem();

        //Create new PlannedNotification object
        NotificationDB notificationDB = new NotificationDB(this.formTitleEditTextField.getText().toString(),
                this.formMessageEditTextField.getText().toString(),
                new Date(calendar.getTimeInMillis()),
                channel.notificationChannelID
        );
        //Insert PlannedNotification object into the database
        notificationsViewModel.insert(notificationDB);
        Navigation.findNavController(v).navigate(R.id.action_createNotificationFragment_to_notificationListFragment);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_create_notification, container, false);
        super.setup();
        asyncGetDataFromDatabase();
        return v;
    }

    private void asyncGetDataFromDatabase(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        //Start an Asynctask to get the Notification and notificationChannels from the database
        executor.execute(() -> {
            AppDatabase appDatabase = AppDatabase.getInstance(getActivity().getApplication());
            NotificationChannelDBDao notificationChannelDBDao = appDatabase.notificationChannelDBDao();
            List<NotificationChannelDB> notificationChannels = notificationChannelDBDao.getAllAsList();

            //Handler performs UI Thread work
            handler.post(()-> setNotificationChannels(notificationChannels)
            );
        });
    }



}