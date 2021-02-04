package com.example.tripreminder.ui.fragment.upcoming;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tripreminder.data.model.db.Note;
import com.example.tripreminder.data.model.db.TripRepository;
import com.example.tripreminder.data.model.db.Trips;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class UpComingViewModel extends AndroidViewModel {

    private TripRepository mRepository;
    private ExecutorService executorService;

    private  LiveData<List<Trips>> allTrips;
    private  LiveData<List<Trips>> allHistory;
    private  LiveData<Integer> tripId;
    private  LiveData<List<String>> allNotes = null;


    public UpComingViewModel ( Application application,String userEmail) {
        super(application);
        mRepository = new TripRepository(application,userEmail);
        allTrips = mRepository.getAllTrips();
        allHistory = mRepository.getAllHistoryTrips();

    }

    LiveData<List<Trips>> getAllTrips() {
        return allTrips;
    }

    LiveData<List<Trips>> getAllHistory() {
        return allHistory;
    }

    LiveData<List<String>> getAllNotes(int id){
        if(allNotes == null) {
            allNotes = mRepository.getAllNotes(id);
        }
        return allNotes;
    }

    public void deleteTrip(int tripId){
        mRepository.deleteTrip(tripId);
    }

      public void insert(Trips trip) {
            mRepository.insertTrips(trip);
    }

    public void updateTrip(String status, int id) { mRepository.updateTrip(status,id); }
    public void insertNote(List<Note> notes,int tripId) { mRepository.setNote(notes,tripId); }

}
