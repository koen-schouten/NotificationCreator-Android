package com.koenschouten.notificationcreator.listadapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.koenschouten.notificationcreator.database.tables.NotificationChannelDB;

import java.util.List;

public class NotificationChannelSpinnerAdapter extends ArrayAdapter<NotificationChannelDB> {
    private final List<NotificationChannelDB> notificationChannelList;

    public NotificationChannelSpinnerAdapter(Context context, int resource, int textViewResourceId, List<NotificationChannelDB> notificationChannelList) {
        super(context, resource , textViewResourceId, notificationChannelList);
        this.notificationChannelList = notificationChannelList;
    }

    @Override
    public int getCount() {
        return notificationChannelList.size();
    }

    @Override
    public NotificationChannelDB getItem(int i) {
        return notificationChannelList.get(i);
    }
}
