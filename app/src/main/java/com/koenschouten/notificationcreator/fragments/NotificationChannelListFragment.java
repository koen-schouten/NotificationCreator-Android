package com.koenschouten.notificationcreator.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.koenschouten.notificationcreator.R;
import com.koenschouten.notificationcreator.database.tables.NotificationChannelDB;
import com.koenschouten.notificationcreator.listadapters.NotificationChannelAdapter;
import com.koenschouten.notificationcreator.utils.RecyclerItemClickListener;
import com.koenschouten.notificationcreator.viewModels.NotificationChannelViewModel;

import org.jetbrains.annotations.NotNull;

public class NotificationChannelListFragment extends Fragment {
    private View v;
    private NotificationChannelViewModel notificationChannelViewModel;
    private RecyclerView notificationChannelRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setupRecyclerView(){

    }

    private void addItemTouchListenerToNotificationChannelRecyclerView(){

    }



    private void setupCreateNotificationChannelButton(){
        notificationChannelRecyclerView = v.findViewById(R.id.recyclerview_notification_channels);

        final NotificationChannelAdapter notificationAdapter = new NotificationChannelAdapter(new NotificationChannelAdapter.NotificationChannelDiff());

        notificationChannelRecyclerView.setAdapter(notificationAdapter);
        notificationChannelRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //Makes the recyclerView observe changes from the PlannedNotificationViewModel
        notificationChannelViewModel.getAllNotificationChannels().observe(getViewLifecycleOwner(), notificationAdapter::submitList);

        addItemTouchListenerToNotificationChannelRecyclerView();
    }

    private void setupGotoCreateNotificationButton(){
        FloatingActionButton gotoCreateNotificationButton =  v.findViewById(R.id.goto_create_notification_channel_button);
        gotoCreateNotificationButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_notificationChannelListFragment_to_createNotificationChannelFragment));
    }

    private void addItemTouchListenerToNotificationsRecyclerView() {
        notificationChannelRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), notificationChannelRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        NotificationChannelDB clickedNotificationChannelDB = notificationChannelViewModel.getNotificationChannelByIndex(position);


                        String notificationChannelID = String.valueOf(clickedNotificationChannelDB.notificationChannelID);
                        Intent settingsIntent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                                .putExtra(Settings.EXTRA_CHANNEL_ID, notificationChannelID)
                                .putExtra(Settings.EXTRA_APP_PACKAGE, getContext().getPackageName());
                        startActivity(settingsIntent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }




    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        notificationChannelViewModel = new ViewModelProvider(this).get(NotificationChannelViewModel.class);
        
        v = inflater.inflate(R.layout.fragment_notification_channel_list, container, false);
        setupGotoCreateNotificationButton();
        setupRecyclerView();
        setupCreateNotificationChannelButton();
        addItemTouchListenerToNotificationsRecyclerView();
        return v;
    }
}
