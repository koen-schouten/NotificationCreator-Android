package com.koenschouten.notificationcreator.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.navigation.Navigation;

import com.koenschouten.notificationcreator.R;
import com.koenschouten.notificationcreator.database.AppDatabase;
import com.koenschouten.notificationcreator.database.tables.NotificationChannelDB;
import com.koenschouten.notificationcreator.database.tables.NotificationChannelDBDao;
import com.koenschouten.notificationcreator.database.tables.NotificationDB;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class EditNotificationFragment extends BaseEditNotificationFragment {
    public static final String BUNDLE_NOTIFICATION_ID = "navigation-uid";
    private long notificationID;
    private NotificationDB notificationDB;

    public EditNotificationFragment() {
        // Required empty public constructor
    }

    public void setNotificationDB(NotificationDB notificationDB){
        this.notificationDB = notificationDB;
        this.setUpFormFieldsFromNotificationDB();
    }

    private void setUpFormFieldsFromNotificationDB(){
        calendar.setTime(notificationDB.notificationDate);
        formTitleEditTextField.setText(notificationDB.contentTitle);
        formMessageEditTextField.setText(notificationDB.contentMessage);
        updateDateFieldLabel();
        updateTimeFieldLabel();
        setupFormDateField();
        setupFormTimeField();
        setupSubmitFormButton();
    }


    @Override
    protected void submitForm(){
        notificationDB.update(EditNotificationFragment.this.formTitleEditTextField.getText().toString(),
                EditNotificationFragment.this.formMessageEditTextField.getText().toString(),
                new Date(calendar.getTimeInMillis()));
        notificationsViewModel.update(notificationDB);
        Navigation.findNavController(v).navigate(R.id.action_editNotificationFragment_to_notificationListFragment);
    }

    private int getNotificationChannelPosition(){
        for(NotificationChannelDB channel : notificationChannels){
            if(notificationDB.notificationChannelId == channel.notificationChannelID){
                return notificationChannels.indexOf(channel);
            }
        }
        return -1;
    }

    private void setupDeleteButton(){
        Button deleteButton = v.findViewById(R.id.delete_notification_form_submit_button);
        deleteButton.setOnClickListener(v -> new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.delete_alert_dialog_title))
                .setMessage(getString(R.string.delete_alert_dialog_message))
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    //Delete notification
                    notificationsViewModel.setFlagToDelete(notificationDB);
                    //Go back to notification list screen
                    Navigation.findNavController(v).navigate(R.id.action_editNotificationFragment_to_notificationListFragment);
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.drawable.ic_baseline_report_problem_24)
                .show());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_edit_notification, container, false);
        notificationID = getArguments().getLong(BUNDLE_NOTIFICATION_ID);
        super.setup();
        setupDeleteButton();
        asyncGetDataFromDatabase();
        return v;
    }

    private void asyncGetDataFromDatabase(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        //Start an Async task to get the Notification and notificationChannels from the database
        executor.execute(() -> {
            //Get notification from database
            NotificationDB notification = notificationsViewModel.getNotificationByID(notificationID);
            //Get notification Channel from database
            AppDatabase appDatabase = AppDatabase.getInstance(this.getActivity().getApplication());
            NotificationChannelDBDao notificationChannelDBDao = appDatabase.notificationChannelDBDao();
            List<NotificationChannelDB> notificationChannels = notificationChannelDBDao.getAllAsList();

            //Handles performs UI Thread work
            handler.post(() -> {
                        EditNotificationFragment.this.setNotificationDB(notification);
                        EditNotificationFragment.this.setNotificationChannels(notificationChannels);
                        formChannelSpinner.setSelection(getNotificationChannelPosition());
                    }
            );
        });
    }
}