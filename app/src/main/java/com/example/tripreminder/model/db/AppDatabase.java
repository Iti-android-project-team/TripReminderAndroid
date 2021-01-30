package com.example.tripreminder.model.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Trips.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TripDao userDao();

}
