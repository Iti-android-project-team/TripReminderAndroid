package com.example.tripreminder.data.model.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TripRepository {

    private TripDao tripDao;
    private static TripRepository mInstance;
    private LiveData<List<Trips>> allTrips;
    private LiveData<List<Trips>> allHistoryTrips;
    private LiveData<List<String>> allNotes = null;
    private String status;

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

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insertTrips(Trips trip) {
        TripRoomDatabase.databaseWriteExecutor.execute(() -> {
            tripDao.insertTrip(trip);
        });
    }

    public void insertNote(List<Notes>notes) {
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

    public void setNote(List<Note> note, int tripId) {
        TripRoomDatabase.databaseWriteExecutor.execute(() -> {
            tripDao.setNote(note, tripId);
        });
    }

    public void updateTrip(String tripName, String startPoint, String endPoint,
                           String date ,String time, String repeated,boolean direction,int tripId){
        TripRoomDatabase.databaseWriteExecutor.execute(() -> {
            tripDao.updateTrip(tripName,startPoint,endPoint,date,time,repeated,direction,tripId);
        });
    }



}
