package com.koenschouten.notificationcreator.database;

import androidx.room.TypeConverter;


import com.koenschouten.notificationcreator.database.tables.NotificationChannelDB;

import java.util.Date;


public class Converters {
    @TypeConverter
    public static Date TimestampToDate(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static int channelImportanceEnumToInt(NotificationChannelDB.ChannelImportance channelImportance) {
        return channelImportance == null ? 0 : channelImportance.getImportance();
    }

    @TypeConverter
    public static NotificationChannelDB.ChannelImportance channelImportanceEnumToInt(int importance) {
        return importance == 0 ? null : NotificationChannelDB.ChannelImportance.valueOfLabel(importance);
    }
}