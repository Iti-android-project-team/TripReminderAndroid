package com.example.tripreminder.ui.fragment.history;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tripreminder.data.model.db.TripRepository;
import com.example.tripreminder.data.model.db.Trips;

import java.util.List;

public class HistoryViewModel  extends AndroidViewModel {


    private LiveData<List<Trips>> allHistory;
    private TripRepository mRepository;

    public HistoryViewModel(@NonNull Application application,String userEmail) {
        super(application);
        mRepository = new TripRepository(application,userEmail);
        allHistory = mRepository.getAllHistoryTrips();

    }

    LiveData<List<Trips>> getAllHistory() {
        return allHistory;
    }

    public void deleteTrip(String status ,int tripId){
        mRepository.updateTrip(status,tripId);
    }
}
