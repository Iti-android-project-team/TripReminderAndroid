package com.example.tripreminder.data.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table"
//        ,foreignKeys = @ForeignKey(entity = Trips.class,
//        parentColumns = "tripe_id",
//        childColumns = "tripe",
//        onDelete = CASCADE)
)

public class Notes {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    private int noteId;

    @ColumnInfo(name = "note")
    private String notes;

    @ColumnInfo(name = "tripe")
    public int tripeId;

    @ColumnInfo(name = "note_status")
    public String noteStatus;

    public int getTripeId() {
        return tripeId;
    }

    public void setTripeId(int tripeId) {
        this.tripeId = tripeId;
    }

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
