package com.example.tripreminder.ui.activities.dialog;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.tripreminder.data.model.db.TripRepository;

public class DialogViewModel extends AndroidViewModel {
    private TripRepository mRepository;

    public DialogViewModel(@NonNull Application application ,String userEmail) {
        super(application);
        mRepository = new TripRepository(application,userEmail);
    }

    public void updateTrip(String status, int id) {
        mRepository.updateTrip(status,id);
    }
}