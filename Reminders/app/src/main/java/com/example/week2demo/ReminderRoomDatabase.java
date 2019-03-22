package com.example.week2demo;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Reminder.class}, version = 1, exportSchema = false)
public abstract class ReminderRoomDatabase extends RoomDatabase {
    private final static String NAME_DATABASE = "reminder_database";

    public abstract ReminderDAO reminderDao();

    private static volatile ReminderRoomDatabase INSTANCE;

    static ReminderRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ReminderRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ReminderRoomDatabase.class, NAME_DATABASE)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}