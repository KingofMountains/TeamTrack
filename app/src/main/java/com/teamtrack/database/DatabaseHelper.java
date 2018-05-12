package com.teamtrack.database;

import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * Created by GIRIN on 25-Mar-18.
 */

public class DatabaseHelper {

    private static AppDatabase appDatabase;

    public static AppDatabase getInstance(Context context) {

        try {
            if (appDatabase == null) {
                appDatabase = Room.databaseBuilder(context, AppDatabase.class, "team-track-db").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return appDatabase;
    }
}
