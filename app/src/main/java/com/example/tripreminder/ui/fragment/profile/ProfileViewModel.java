package com.example.tripreminder.ui.fragment.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tripreminder.data.model.db.TripRepository;
import com.example.tripreminder.data.model.db.Trips;

import java.util.List;

public class ProfileViewModel extends AndroidViewModel {


    private LiveData<List<Trips>> allTrips;
    private TripRepository mRepository;

    public ProfileViewModel(@NonNull Application application, String userEmail) {
        super(application);
        mRepository = new TripRepository(application, userEmail);
        allTrips = mRepository.getAllTripsForFirebase();

    }

    LiveData<List<Trips>> getAllTrips() {
        return allTrips;
    }

}
