package com.example.tripreminder.model.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TripRepository {

    private final TripDao tripDao;
    private final LiveData<List<Trips>> allTrips;
    private final LiveData<List<Trips>> allHistoryTrip;
    private LiveData<List<String>> allNotes = null;


    public TripRepository(Application application, String userEmail) {
        TripRoomDatabase db = TripRoomDatabase.getDatabase(application);
        tripDao = db.tripDao();
        allTrips = tripDao.getUpComingTrips(userEmail);
        allHistoryTrip = tripDao.getHistoryTrips(userEmail);
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Trips>> getAllTrips() {
        return allTrips;
    }

    public LiveData<List<Trips>> getAllHistoryTrips() {

        return allHistoryTrip;
    }

    public LiveData<List<String>> getAllNotes(int id) {
        if (allNotes == null) {
            allNotes = tripDao.getNotes(id);
        }
        return allNotes;
    }


    public String getWorkManagerTag(String userEmail, int tripId) {
        String workTag = tripDao.getWorkManagerTag(userEmail, tripId);
        return workTag;
    }

    public void deleteTrip(int tripId) {
        TripRoomDatabase.databaseWriteExecutor.execute(() -> {
            tripDao.deleteTrip(tripId);
        });
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

    public void setNote(List<Note> note, int tripId) {
        TripRoomDatabase.databaseWriteExecutor.execute(() -> {
            tripDao.setNote(note, tripId);
        });
    }

}
