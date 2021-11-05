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
import com.koenschouten.notificationcreator.database.tables.NotificationDB;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

    public class NotificationAdapter extends ListAdapter<NotificationDB, NotificationAdapter.NotificationHolder> {

    public NotificationAdapter(@NonNull DiffUtil.ItemCallback<NotificationDB> diffCallback) {
        super(diffCallback);
    }


    @Override
    public @NotNull NotificationAdapter.NotificationHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return NotificationHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(NotificationHolder holder, int position) {
        NotificationDB current = getItem(position);
        holder.bind(current.contentTitle, current.contentMessage, current.notificationDate);
    }

    /**
     * Is used to calculate the difference between the notification lists when the notification list
     * changes. This class is necessary to create a smooth update when deleting an item from the list.
     */
    public static class NotificationDiff extends DiffUtil.ItemCallback<NotificationDB> {
        @Override
        public boolean areItemsTheSame(@NonNull NotificationDB oldItem, @NonNull NotificationDB newItem) {
            return oldItem.notificationID == newItem.notificationID;
        }

        @Override
        public boolean areContentsTheSame(@NonNull NotificationDB oldItem, @NonNull NotificationDB newItem) {
            return oldItem.notificationID == newItem.notificationID;
        }
    }

    public static class NotificationHolder extends RecyclerView.ViewHolder {
        private final TextView notificationTitleView;
        private final TextView notificationMessageView;
        private final TextView notificationDateView;

        private NotificationHolder(View itemView){
            super(itemView);
            notificationTitleView = itemView.findViewById(R.id.notification_title);
            notificationMessageView = itemView.findViewById(R.id.notification_message);
            notificationDateView = itemView.findViewById(R.id.notification_date);
        }

        private void setDateViewText(Date date) {
            String dateFormat = "EEE dd MMMM yyyy HH:mm";
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
            notificationDateView.setText(sdf.format(date));
        }


        public void bind(String title, String message, Date date) {
            notificationTitleView.setText(title);
            notificationMessageView.setText(message);
            setDateViewText(date);
        }

        static NotificationHolder create(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_notification_item, parent, false);
            return new NotificationHolder(view);
        }
    }
}
