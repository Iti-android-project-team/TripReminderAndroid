package com.example.tripreminder.data.model.db;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import io.grpc.SynchronizationContext;

public class TripRepository {

    private TripDao tripDao;
    private LiveData<List<Trips>> allTrips;
    private LiveData<List<Trips>> allHistoryTrips;
    private LiveData<List<String>> allNotes = null;
    private String status;
    private LiveData<Integer>tripId;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public TripRepository(Application application) {
        TripRoomDatabase db = TripRoomDatabase.getDatabase(application);
        tripDao = db.tripDao();
        allTrips = tripDao.getUpComingTrips();
        allHistoryTrips= tripDao.getHistoryTrips();
    }


    public TripRepository(Application application,int id) {
        TripRoomDatabase db = TripRoomDatabase.getDatabase(application);
        tripDao = db.tripDao();

    }


    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Trips>> getAllTrips() {
        return allTrips;
    }
    public LiveData<List<Trips>> getAllHistoryTrips() {
        return allHistoryTrips;
    }

    public LiveData<List<String>> getAllNotes(int id) {
        if(allNotes == null){
            allNotes = tripDao.getNotes(id);
        }
        return allNotes;
    }

    public int getTripId(){
        int tripID = tripDao.getTripId();
        return tripID;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
      public void insertTrips(Trips trip) {
          TripRoomDatabase.databaseWriteExecutor.execute(() -> {
              tripDao.insertTrip(trip);
          });
    }

    public void updateTrip(String status, int tripId) {
        TripRoomDatabase.databaseWriteExecutor.execute(() -> {
            tripDao.updateTripStatus(status, tripId);
        });
    }
    public void updateNote(String status, int noteId) {
        TripRoomDatabase.databaseWriteExecutor.execute(() -> {
            tripDao.updateNoteStatus(status, noteId);
        });
    }

    public void setNote(List<Note> note, int tripId) {
        TripRoomDatabase.databaseWriteExecutor.execute(() -> {
            tripDao.setNote(note, tripId);
        });
    }


}
