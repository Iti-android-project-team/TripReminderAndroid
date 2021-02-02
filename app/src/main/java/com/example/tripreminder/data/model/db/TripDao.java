package com.example.tripreminder.data.model.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TripDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertTrip(Trips trips);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertNote(List<Notes> notes);

    @Query("SELECT * FROM trip_table WHERE status = 'upComing'")
    LiveData<List<Trips>> getUpComingTrips();

    @Query("SELECT * FROM trip_table WHERE status = 'cancled' OR status = 'done'")
    LiveData<List<Trips>> getHistoryTrips();


//    @Query("SELECT * FROM note_table WHERE tripe = :triId")
//    LiveData<List<Notes>> getNotes(int triId);

    @Query("SELECT notes FROM trip_table WHERE tripe_id = :triId")
    LiveData<List<String>> getNotes(int triId);

    @Query("UPDATE trip_table SET status = :status WHERE tripe_id = :tripId")
    void updateTripStatus(String status, int tripId);

    @Query("UPDATE note_table SET note_status = :status WHERE note_id = :noteId")
    void updateNoteStatus(String status, int noteId);

    @Query("UPDATE trip_table SET notes = :note WHERE tripe_id = :tripId")
    void setNote(List<Note> note, int tripId);

}
