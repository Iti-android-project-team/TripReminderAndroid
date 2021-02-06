package com.example.tripreminder.ui.activities.editTrip;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tripreminder.data.model.db.TripRepository;
import com.example.tripreminder.data.model.db.Trips;

import java.util.List;

public class EditTripViewModel extends AndroidViewModel {
    private TripRepository mRepository;

    public EditTripViewModel(@NonNull Application application,String userEmail) {
        super(application);
        mRepository = new TripRepository(application,userEmail);
    }
    public void updateTrip(String tripName, String startPoint, String endPoint,
                           String date ,String time, String repeated,boolean direction,int tripId)
    { mRepository.updateTrip(tripName,startPoint,endPoint,date,time,repeated,direction,tripId); }


}
