package com.example.tripreminder.model.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TripRepository {

    private TripDao tripDao;
    private LiveData<List<Trips>> allTrips;
    private LiveData<List<Notes>> allNotes;
    private String status;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public TripRepository(Application application,String status) {
        TripRoomDatabase db = TripRoomDatabase.getDatabase(application);
        tripDao = db.tripDao();
        allTrips = tripDao.getTrips(status);
    }

    public TripRepository(Application application,int id) {
        TripRoomDatabase db = TripRoomDatabase.getDatabase(application);
        tripDao = db.tripDao();
        allNotes = tripDao.getNotes(id);
    }


    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Trips>> getAllTrips() {
        return allTrips;
    }

    public LiveData<List<Notes>> getAllNotes() {
        return allNotes;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insertTrips(Trips trip) {
        TripRoomDatabase.databaseWriteExecutor.execute(() -> {
            tripDao.insertTrip(trip);
        });
    }

    public void insertNote(Notes... notes) {
        TripRoomDatabase.databaseWriteExecutor.execute(() -> {
            tripDao.insertNote(notes);
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

}
