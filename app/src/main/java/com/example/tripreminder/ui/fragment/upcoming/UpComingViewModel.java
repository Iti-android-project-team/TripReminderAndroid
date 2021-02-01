package com.example.tripreminder.ui.fragment.upcoming;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tripreminder.model.db.Note;
import com.example.tripreminder.model.db.Notes;
import com.example.tripreminder.model.db.TripRepository;
import com.example.tripreminder.model.db.Trips;

import java.util.List;

public class UpComingViewModel extends AndroidViewModel {

    private TripRepository mRepository;

    private final LiveData<List<Trips>> allTrips;
    private  LiveData<List<String>> allNotes = null;


    public UpComingViewModel ( Application application) {
        super(application);
        mRepository = new TripRepository(application);
        allTrips = mRepository.getAllTrips();
    }

    LiveData<List<Trips>> getAllTrips() { return allTrips; }

    LiveData<List<String>> getAllNotes(int id){
        if(allNotes == null) {
            allNotes = mRepository.getAllNotes(id);
        }
        return allNotes;
    }

    public void insert(Trips trip) { mRepository.insertTrips(trip); }
    public void updateTrip(String status, int id) { mRepository.updateTrip(status,id); }
    public void insertNote(List<Note> notes,int tripId) { mRepository.setNote(notes,tripId); }

}
