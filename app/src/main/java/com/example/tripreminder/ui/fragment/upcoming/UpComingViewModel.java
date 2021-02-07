package com.example.tripreminder.ui.fragment.upcoming;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.work.WorkManager;

import com.example.tripreminder.data.model.db.TripRepository;
import com.example.tripreminder.data.model.db.Trips;
import com.example.tripreminder.ui.activities.addTrip.AddTripViewModel;

import java.util.List;




public class UpComingViewModel extends AndroidViewModel {

    private TripRepository mRepository;

    private final LiveData<List<Trips>> allTrips;

    private  LiveData<Integer> tripId;
    private  LiveData<List<String>> allNotes = null;
    private WorkManager tripsWorkManager;



    public UpComingViewModel ( Application application,String userEmail) {
        super(application);
        mRepository = new TripRepository(application,userEmail);
        allTrips = mRepository.getAllTrips();
        tripsWorkManager = WorkManager.getInstance(application);

    }

    LiveData<List<Trips>> getAllTrips() {
        return allTrips;
    }




    public void deleteTrip(int tripId){
        mRepository.deleteTrip(tripId);
    }

      public void insert(Trips trip) {
            mRepository.insertTrips(trip);
    }


    public void updateTrip(String status, int id) { mRepository.updateTrip(status,id); }

    public LiveData<String> getWorkManageTag(String userEmail,int tripId){
        LiveData<String> workTag = mRepository.getWorkManagerTag(userEmail,tripId);
      return workTag;
    }

    public void cancelWorkManager(String tag){
        tripsWorkManager.cancelAllWorkByTag(tag);
    }

}
