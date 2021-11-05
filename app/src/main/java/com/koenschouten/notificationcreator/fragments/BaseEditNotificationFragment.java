package com.koenschouten.notificationcreator.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.koenschouten.notificationcreator.R;
import com.koenschouten.notificationcreator.database.tables.NotificationChannelDB;
import com.koenschouten.notificationcreator.listadapters.NotificationChannelSpinnerAdapter;
import com.koenschouten.notificationcreator.viewModels.NotificationChannelViewModel;
import com.koenschouten.notificationcreator.viewModels.NotificationsViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public abstract class BaseEditNotificationFragment extends Fragment {
    protected EditText formTitleEditTextField;
    protected EditText formMessageEditTextField;
    protected EditText formDateField;
    protected EditText formTimeField;
    protected Spinner formChannelSpinner;
    protected Button submitFormButton;

    final protected Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

    protected View v;

    protected NotificationsViewModel notificationsViewModel;
    protected NotificationChannelViewModel notificationChannelViewModel;

    protected List<NotificationChannelDB> notificationChannels;

    protected void setupFormTimeField(){
        TimePickerDialog.OnTimeSetListener time = (view, hourOfDay, minute) -> {
            BaseEditNotificationFragment.this.calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            BaseEditNotificationFragment.this.calendar.set(Calendar.MINUTE, minute);
            updateTimeFieldLabel();
        };

        formTimeField.setOnClickListener(v -> new TimePickerDialog(getContext(),
                time, BaseEditNotificationFragment.this.calendar.get(Calendar.HOUR_OF_DAY),
                BaseEditNotificationFragment.this.calendar.get(Calendar.MINUTE), true ).show());
    }

    protected void setupFormDateField(){
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            BaseEditNotificationFragment.this.calendar.set(Calendar.YEAR, year);
            BaseEditNotificationFragment.this.calendar.set(Calendar.MONTH, monthOfYear);
            BaseEditNotificationFragment.this.calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateFieldLabel();
        };

        formDateField.setOnClickListener(v -> new DatePickerDialog(getContext(), date, BaseEditNotificationFragment.this.calendar
                .get(Calendar.YEAR), BaseEditNotificationFragment.this.calendar.get(Calendar.MONTH),
                BaseEditNotificationFragment.this.calendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    protected void updateDateFieldLabel() {
        String dateFormat = "EE dd MMMM y";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        formDateField.setText(sdf.format(calendar.getTime()));
    }


    protected void setupSubmitFormButton(){
        submitFormButton.setOnClickListener(v -> submitForm());
    }

    protected void updateTimeFieldLabel() {
        String dateFormat = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        formTimeField.setText(sdf.format(calendar.getTime()));
    }

    protected void setupViewModels(){
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        notificationsViewModel = viewModelProvider.get(NotificationsViewModel.class);
        notificationChannelViewModel = viewModelProvider.get(NotificationChannelViewModel.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setupChannelSpinner(){
        NotificationChannelSpinnerAdapter notificationChannelSpinnerAdapter = new NotificationChannelSpinnerAdapter(getContext(), R.layout.spinner_notification_channel_item, R.id.spinner_text , notificationChannels);
        if(notificationChannels != null && notificationChannels.size()> 0) {
            formChannelSpinner.setAdapter(notificationChannelSpinnerAdapter);
        }
    }

    protected void setNotificationChannels(List<NotificationChannelDB> notificationChannels){
        this.notificationChannels = notificationChannels;
        setupChannelSpinner();
    }

    protected void setupViews(){
        formChannelSpinner = v.findViewById(R.id.edit_notification_form_channel_spinner);
        formTitleEditTextField = v.findViewById(R.id.edit_notification_form_title_text_field);
        formMessageEditTextField = v.findViewById(R.id.edit_notification_form_message_text_field);
        formDateField = v.findViewById(R.id.edit_notification_form_date_field);
        submitFormButton = v.findViewById(R.id.edit_notification_form_submit_button);
        formTimeField = v.findViewById(R.id.edit_notification_form_time_field);
    }

    protected void setup(){
        calendar.setTime(new Date(System.currentTimeMillis()));

        setupViewModels();
        setupViews();
        setupSubmitFormButton();

        updateDateFieldLabel();
        updateTimeFieldLabel();

        setupFormDateField();
        setupFormTimeField();
    }

    public BaseEditNotificationFragment(){
    }

    protected void submitForm(){}
}
