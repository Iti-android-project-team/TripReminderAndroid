package com.example.tripreminder.ui.activities.editTrip;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.tripreminder.data.model.db.TripRepository;
import com.example.tripreminder.data.model.db.Trips;
import com.example.tripreminder.data.services.UpWorkManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class EditTripViewModel extends AndroidViewModel {
    private TripRepository mRepository;

    private WorkManager tripsWorkManager;
    private Data.Builder data = new Data.Builder();

    public EditTripViewModel(@NonNull Application application,String userEmail) {
        super(application);
        mRepository = new TripRepository(application,userEmail);
        tripsWorkManager = WorkManager.getInstance(application);
    }
    public void updateTrip(String tripName, String startPoint, String endPoint,
                           String date ,String time, String repeated,boolean direction,String workManagerTag,int tripId)
    { mRepository.updateTrip(tripName,startPoint,endPoint,date,time,repeated,direction,workManagerTag,tripId); }

    public void addTripWorkOneTime(int duration, TimeUnit timeUnit, String tag, int tripId, String endPoint){
        data.putInt("tripId",tripId);
        data.putString("endPoint",endPoint);
        WorkRequest uploadWorkRequest =
                new OneTimeWorkRequest.Builder(UpWorkManager.class)
                        .setInitialDelay(duration,timeUnit)
                        .addTag(tag)
                        .setInputData(data.build())
                        .build();
        tripsWorkManager.enqueue(uploadWorkRequest);
    }

    public void addTripWorkRepeated(int duration, int repeated,TimeUnit timeUnit,
                                    String tag,int tripId,String endPoint){
        data.putInt("tripId",tripId);
        data.putString("endPoint",endPoint);
        WorkRequest uploadWorkRequest =
                new PeriodicWorkRequest.Builder(UpWorkManager.class,repeated,timeUnit)
                        .setInitialDelay(duration,timeUnit)
                        .addTag(tag)
                        .setInputData(data.build())
                        .build();
        tripsWorkManager.enqueue(uploadWorkRequest);
    }
    public void cancelWorkManager(String tag){
        tripsWorkManager.cancelAllWorkByTag(tag);
    }

}
