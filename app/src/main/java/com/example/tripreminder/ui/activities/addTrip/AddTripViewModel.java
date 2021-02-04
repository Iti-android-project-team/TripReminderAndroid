package com.example.tripreminder.ui.activities.addTrip;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.tripreminder.data.model.db.TripRepository;
import com.example.tripreminder.data.model.db.Trips;
import com.example.tripreminder.data.services.UpWorkManager;

import java.util.concurrent.TimeUnit;

public class AddTripViewModel extends AndroidViewModel {
    private TripRepository mRepository;
    private WorkManager tripsWorkManager;
    private Data.Builder data = new Data.Builder();
    public AddTripViewModel(@NonNull Application application,String userEmail) {
        super(application);
        tripsWorkManager = WorkManager.getInstance(application);
        mRepository = new TripRepository(application,userEmail);
    }


    public void addTripWorkOneTime(int duration, TimeUnit timeUnit,String tag,int tripId){
        data.putInt("tripId",tripId);
        WorkRequest uploadWorkRequest =
                new OneTimeWorkRequest.Builder(UpWorkManager.class)
                        .setInitialDelay(duration,timeUnit)
                        .addTag(tag)
                        .setInputData(data.build())
                        .build();
        tripsWorkManager.enqueue(uploadWorkRequest);
    }

    public void addTripWorkRepeated(int duration, int repeated,TimeUnit timeUnit,String tag){
        WorkRequest uploadWorkRequest =
                new PeriodicWorkRequest.Builder(UpWorkManager.class,repeated,timeUnit)
                        .setInitialDelay(duration,timeUnit)
                        .addTag(tag)
                        .build();
        tripsWorkManager.enqueue(uploadWorkRequest);
    }
    public void insert(Trips trip) {
        mRepository.insertTrips(trip);
    }

    public void cancelWorkManager(String tag){
        tripsWorkManager.cancelAllWorkByTag(tag);
    }

    public int getTripId(){
        return mRepository.getTripId();
    }

}