package com.teamtrack.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.teamtrack.database.tables.Schedule;

import java.util.List;

@Dao
public interface ScheduleDao {

    @Query("SELECT * FROM Schedule")
    List<Schedule> getAll();

    @Insert
    void insertSchedule(Schedule scheduleList);

    @Insert
    void insertSchedules(List<Schedule> schedulesList);

    @Query("UPDATE Schedule set status =:status,remarks = :remarks,sales_location=:salesLocation,sales_location_time=:salesTime " +
            "where schedule_id = :schedule_id")
    void updateSchedule(String status, String remarks, String salesLocation, String salesTime, int schedule_id);
}