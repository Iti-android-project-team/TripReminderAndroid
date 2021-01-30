package com.example.tripreminder.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.tripreminder.model.Notes;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

@Entity
public class Trips {
    @PrimaryKey
    public int tid;

    @ColumnInfo(name = "tripe_name")
    public String tripName;

    @ColumnInfo(name = "start_point")
    public String startPoint;

    @ColumnInfo(name = "end_point")
    public String endPoint;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "time")
    public String time;

    @ColumnInfo(name = "repeated")
    public String repeated;

    @ColumnInfo(name = "direction")
    public boolean direction;

    @ColumnInfo(name = "notes")
    public List<Notes> notes;

    @ColumnInfo(name = "status")
    public String status;

}
