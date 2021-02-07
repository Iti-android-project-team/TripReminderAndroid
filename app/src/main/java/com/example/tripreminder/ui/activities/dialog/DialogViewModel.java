package com.example.tripreminder.ui.activities.dialog;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tripreminder.data.model.db.Note;
import com.example.tripreminder.data.model.db.TripRepository;

import java.util.List;

public class DialogViewModel extends AndroidViewModel {
    private TripRepository mRepository;
    private LiveData<List<Note>> notes;



    public DialogViewModel(@NonNull Application application, String userEmail) {
        super(application);
        mRepository = new TripRepository(application,userEmail);
    }

    public void updateTrip(String status, int id) {
        mRepository.updateTrip(status,id);
    }

    public LiveData<List<Note>> getNote(int id){
        notes = mRepository.getAllNotes(id);
        return notes;
    }
}