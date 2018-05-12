package com.teamtrack.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.teamtrack.database.tables.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Insert
    void insertUser(List<User> userList);

}