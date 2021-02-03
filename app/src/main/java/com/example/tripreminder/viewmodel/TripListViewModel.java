package com.example.tripreminder.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tripreminder.model.db.Note;
import com.example.tripreminder.model.db.Notes;
import com.example.tripreminder.model.db.TripRepository;
import com.example.tripreminder.model.db.Trips;

import java.util.List;


public class TripListViewModel extends AndroidViewModel {

    private TripRepository mRepository;

    public TripListViewModel(@NonNull Application context) {
        super(context);
        mRepository = new TripRepository(context);
    }

    public void insert(Trips trip) {
       mRepository.insertTrips(trip);
    }






}
