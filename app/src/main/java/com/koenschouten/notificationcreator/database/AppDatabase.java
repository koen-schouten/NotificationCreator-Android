package com.koenschouten.notificationcreator.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.koenschouten.notificationcreator.database.tables.NotificationChannelDBDao;
import com.koenschouten.notificationcreator.database.tables.NotificationDB;
import com.koenschouten.notificationcreator.database.tables.NotificationDBDao;
import com.koenschouten.notificationcreator.database.tables.NotificationChannelDB;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {NotificationDB.class, NotificationChannelDB.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();
    private static final String DATABASE_NAME = "notification_database";
    private static volatile AppDatabase instance;


    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            //Executed when database is created
        }

    };

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context,
                    AppDatabase.class, DATABASE_NAME).addCallback(sRoomDatabaseCallback)
                    .build();
        }
        return instance;
    }

    public abstract NotificationDBDao notificationDBDao();
    public abstract NotificationChannelDBDao notificationChannelDBDao();

}
