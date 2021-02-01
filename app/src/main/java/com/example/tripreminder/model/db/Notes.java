package com.example.tripreminder.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table",foreignKeys = @ForeignKey(entity = Trips.class,
        parentColumns = "note_id",
        childColumns = "tripe_id",
        onDelete = ForeignKey.NO_ACTION),indices = {@Index(value = {"tripe_id"})})

public class Notes {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    private int noteId;

    @ColumnInfo(name = "note")
    private String notes;

    @ColumnInfo(name = "tripe_id")
    public int tripeId;

    @ColumnInfo(name = "note_status")
    public String noteStatus;



    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
