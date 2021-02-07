package com.example.tripreminder.data.model.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

//import io.reactivex.rxjava3.core.Single;

public class TripRepository {

    private TripDao tripDao;
    private static TripRepository mInstance;
    private final LiveData<List<Trips>> allTrips;
    private final LiveData<List<Trips>> getTrips;
    private final LiveData<List<Trips>> allHistoryTrip;
    private final LiveData<Integer> tripId;

    private LiveData<List<String>> allNotes = null;


    public TripRepository(Application application, String userEmail) {
        TripRoomDatabase db = TripRoomDatabase.getDatabase(application);
        tripDao = db.tripDao();
        allTrips = tripDao.getUpComingTrips(userEmail);
        allHistoryTrip = tripDao.getHistoryTrips(userEmail);
        getTrips = tripDao.getTrips(userEmail);
        tripId = tripDao.getTripId();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Trips>> getAllTrips() {
        return allTrips;
    }

    public LiveData<List<Trips>> getAllHistoryTrips() {

        return allHistoryTrip;
    }
    public LiveData<List<Trips>> getAllTripsForFirebase() { return allTrips; }

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
    public long insertTrips(Trips trip) {
        final long[] tripId = new long[1];
        TripRoomDatabase.databaseWriteExecutor.execute(() -> {
            tripId[0] = tripDao.insertTrip(trip);
        });
        return tripId[0];
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


    public LiveData<Integer> getTripId(){
        return tripId;
    }


    public void updateTrip(String tripName, String startPoint, String endPoint,
                           String date ,String time, String repeated,boolean direction,int tripId){
        TripRoomDatabase.databaseWriteExecutor.execute(() -> {
            tripDao.updateTrip(tripName,startPoint,endPoint,date,time,repeated,direction,tripId);
        });
    }

}
