package com.koenschouten.notificationcreator.fragments;

import android.app.NotificationChannel;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.koenschouten.notificationcreator.R;
import com.koenschouten.notificationcreator.database.tables.NotificationChannelDB;
import com.koenschouten.notificationcreator.database.tables.NotificationDB;
import com.koenschouten.notificationcreator.viewModels.NotificationChannelViewModel;
import com.koenschouten.notificationcreator.viewModels.NotificationsViewModel;

import java.util.Date;


public class CreateNotificationChannelFragment extends Fragment {

    private View v;
    private EditText formTitleEditTextField;
    private EditText formDescriptionTextField;
    private Spinner formChannelImportanceSpinner;
    private Button submitButton;

    private NotificationChannelViewModel notificationChannelViewModel;


    public CreateNotificationChannelFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setupFormFields(){

        formTitleEditTextField = v.findViewById(R.id.create_notification_channel_form_title_text_field);
        formDescriptionTextField = v.findViewById(R.id.create_notification_channel_form_description_text_field);

        formChannelImportanceSpinner = v.findViewById(R.id.create_notification_channel_form_importance_spinner);
        formChannelImportanceSpinner.setAdapter(new ArrayAdapter<>(getContext(), R.layout.spinner_notification_channel_item, R.id.spinner_text, NotificationChannelDB.ChannelImportance.values()));

        formChannelImportanceSpinner.setSelection(2);
    }

    protected void setupViewModels(){
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        notificationChannelViewModel = viewModelProvider.get(NotificationChannelViewModel.class);
    }

    private void setupSubmitButton(){
        submitButton = v.findViewById(R.id.submit_button);

        submitButton.setOnClickListener(v -> {
            submitForm();
        });
    }

    private void submitForm(){
        //Get the selected Importance
        NotificationChannelDB.ChannelImportance importance = (NotificationChannelDB.ChannelImportance) formChannelImportanceSpinner.getSelectedItem();
        //Create new notificationChannelDB object
        NotificationChannelDB notificationChannelDB = new NotificationChannelDB(this.formTitleEditTextField.getText().toString(),
                this.formDescriptionTextField.getText().toString(),
                importance,
                new Date(System.currentTimeMillis())
        );
        //insert notificationChannelDB object into db
        notificationChannelViewModel.insert(notificationChannelDB);
        //Navigate to NotificationChannelListFragment
        Navigation.findNavController(v).navigate(R.id.action_createNotificationChannelFragment_to_notificationChannelListFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_create_notification_channel, container, false);
        setupViewModels();
        setupFormFields();
        setupSubmitButton();
        return v;
    }
}