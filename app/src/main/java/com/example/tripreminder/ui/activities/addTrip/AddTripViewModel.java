package com.example.tripreminder.ui.activities.addTrip;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.tripreminder.data.services.UpWorkManager;

import java.util.concurrent.TimeUnit;

public class AddTripViewModel extends AndroidViewModel {

    private WorkManager tripsWorkManager;

    public AddTripViewModel(@NonNull Application application) {
        super(application);
        tripsWorkManager = WorkManager.getInstance(application);
    }


    public void addTripWorkOneTime(int duration, TimeUnit timeUnit,String tag){
        WorkRequest uploadWorkRequest =
                new OneTimeWorkRequest.Builder(UpWorkManager.class)
                        .setInitialDelay(duration,timeUnit)
                        .addTag(tag)
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

}