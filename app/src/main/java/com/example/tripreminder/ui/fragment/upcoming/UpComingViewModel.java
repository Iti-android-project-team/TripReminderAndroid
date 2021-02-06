package com.example.tripreminder.ui.fragment.upcoming;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tripreminder.data.model.db.Note;
import com.example.tripreminder.data.model.db.TripRepository;
import com.example.tripreminder.data.model.db.Trips;

import java.util.List;

public class UpComingViewModel extends AndroidViewModel {

    private TripRepository mRepository;

    private final LiveData<List<Trips>> allTrips;


    public UpComingViewModel ( Application application) {
        super(application);
        mRepository = new TripRepository(application);
        allTrips = mRepository.getAllTrips();
    }

    LiveData<List<Trips>> getAllTrips() { return allTrips; }


    public void insert(Trips trip) { mRepository.insertTrips(trip); }
    public void updateTrip(String status, int id) { mRepository.updateTrip(status,id); }


}
