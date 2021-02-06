package com.example.tripreminder.ui.activities.addNote;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tripreminder.data.model.db.Note;
import com.example.tripreminder.data.model.db.TripRepository;
import com.example.tripreminder.data.model.db.Trips;

import java.util.List;

public class AddNoteViewModel extends AndroidViewModel {



    private TripRepository mRepository;
    public AddNoteViewModel(@NonNull Application application,String userEmail) {
        super(application);
        mRepository = new TripRepository(application,userEmail);
    }



    public void insertNote(List<Note> notes, int tripId) { mRepository.setNote(notes,tripId); }
}
