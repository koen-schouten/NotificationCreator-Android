package com.koenschouten.notificationcreator.listadapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.koenschouten.notificationcreator.R;
import com.koenschouten.notificationcreator.database.tables.NotificationChannelDB;

import org.jetbrains.annotations.NotNull;

public class NotificationChannelAdapter extends ListAdapter<NotificationChannelDB, NotificationChannelAdapter.NotificationChannelHolder> {


    public NotificationChannelAdapter(@NonNull @NotNull DiffUtil.ItemCallback<NotificationChannelDB> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @NotNull
    @Override
    public NotificationChannelHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return NotificationChannelAdapter.NotificationChannelHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NotificationChannelAdapter.NotificationChannelHolder holder, int position) {
        NotificationChannelDB current = getItem(position);
        String importance = current.importance.toString();

        holder.bind(current.title, importance);
    }

    public static class NotificationChannelDiff extends DiffUtil.ItemCallback<NotificationChannelDB> {
        @Override
        public boolean areItemsTheSame(@NonNull NotificationChannelDB oldItem, @NonNull NotificationChannelDB newItem) {
            return oldItem.notificationChannelID == newItem.notificationChannelID;
        }

        @Override
        public boolean areContentsTheSame(@NonNull NotificationChannelDB oldItem, @NonNull NotificationChannelDB newItem) {
            return oldItem.notificationChannelID == newItem.notificationChannelID;
        }
    }

    public static class NotificationChannelHolder extends RecyclerView.ViewHolder{
        private final TextView notificationChannelTitleView;
        private final TextView notificationChannelImportanceView;

        public NotificationChannelHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            notificationChannelTitleView = itemView.findViewById(R.id.notification_channel_title);
            notificationChannelImportanceView = itemView.findViewById(R.id.notification_channel_importance);

        }

        public void bind(String title, String importance){
            notificationChannelTitleView.setText(title);
            notificationChannelImportanceView.setText(importance);
        }

        static NotificationChannelHolder create(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_notification_channel_item, parent, false);
            return new NotificationChannelHolder(view);
        }
    }

}
