package com.example.tripreminder.model.db;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface TripDao {

    @Insert
    void insertAll(Trips... trips);
}
