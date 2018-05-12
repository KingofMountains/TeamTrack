package com.teamtrack.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.teamtrack.database.dao.ScheduleDao;
import com.teamtrack.database.dao.UserDao;
import com.teamtrack.database.tables.Schedule;
import com.teamtrack.database.tables.User;

@Database(entities = {User.class, Schedule.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ScheduleDao scheduleDao();
}