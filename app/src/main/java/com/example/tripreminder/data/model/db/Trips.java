package com.example.tripreminder.data.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "trip_table")
//,indices = {@Index(value = {"tripe_id"}, unique = true)}
public class Trips {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tripe_id")
    private int tripId;

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    @ColumnInfo(name = "tripe_name")
    private String tripName;

    @ColumnInfo(name = "start_point")
    private String startPoint;

    @ColumnInfo(name = "end_point")
    private String endPoint;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "time")
    private String time;

    @ColumnInfo(name = "repeated")
    private String repeated;

    @ColumnInfo(name = "direction")
    private boolean direction;

    @ColumnInfo(name = "notes")
    private List<Note> notes;

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    @ColumnInfo(name = "status")
    private String status;


//    @ColumnInfo(name = "note_id")
//    public int noteId;
/*
 public Trips(){

 }
    public Trips(int tripId,String tripName, String tripStartLocation, String tripEndLocation, String time,String date, String tripStatus, boolean tripIsRound, String tripRepeat, List<Note> tripNotes) {
        this.tripName = tripName;
        this.startPoint = tripStartLocation;
        this.endPoint = tripEndLocation;
        this.date = date;
        this.time = time;
        this.status = tripStatus;
        this.direction = tripIsRound;
        this.repeated = tripRepeat;
        this.notes = tripNotes;
        this.tripId = tripId;
    }*/

    public int getTid() {
        return tripId;
    }

    public void setTid(int tid) {
        this.tripId = tid;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRepeated() {
        return repeated;
    }

    public void setRepeated(String repeated) {
        this.repeated = repeated;
    }

    public boolean isDirection() {
        return direction;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
