package com.example.tripreminder.ui.fragment.upcoming;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tripreminder.model.db.TripRepository;
import com.example.tripreminder.model.db.Trips;

import java.util.List;

public class UpComingViewModel extends AndroidViewModel {

    private TripRepository mRepository;

    private final LiveData<List<Trips>> allTrips;
    //private final LiveData<List<Notes>> allNotes;


    public UpComingViewModel ( Application application) {
        super(application);
        mRepository = new TripRepository(application,"Test");
        allTrips = mRepository.getAllTrips();
    }

    LiveData<List<Trips>> getAllTrips() { return allTrips; }

    public void insert(Trips trip) { mRepository.insertTrips(trip); }
}
