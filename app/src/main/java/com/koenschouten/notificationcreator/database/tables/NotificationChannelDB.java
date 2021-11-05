package com.koenschouten.notificationcreator.database.tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.Date;


@Entity
public class NotificationChannelDB {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "notification_channel_id")
    public long notificationChannelID;


    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "importance")
    public ChannelImportance importance;

    @ColumnInfo(name = "date_created")
    public Date date_created;

    @ColumnInfo(name = "flag_deleted", defaultValue = "false")
    private boolean flagDeleted;

    public boolean getFlagDeleted(){
        return flagDeleted;
    }

    public void setFlagDeleted(boolean flag){
        flagDeleted = flag;
    }

    public NotificationChannelDB(String title, String description, ChannelImportance importance, Date date_created){
        this.title = title;
        this.description = description;
        this.importance = importance;
        this.date_created = date_created;
        this.flagDeleted = false;
    }

    public enum ChannelImportance {
        IMPORTANCE_LOW(2),
        IMPORTANCE_DEFAULT(3),
        IMPORTANCE_HIGH(4),
        IMPORTANCE_MAX(5);

        public final int importance;

        ChannelImportance(int importance) {
            this.importance = importance;
        }

        public int getImportance() {
            return importance;
        }

        public static ChannelImportance valueOfLabel(int importance) {
            for (ChannelImportance e : values()) {
                if (e.importance == importance) {
                    return e;
                }
            }
            return null;
        }
    }

    @Override
    public @NotNull String toString(){
        return this.title;
    }
}
