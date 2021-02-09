package com.example.tripreminder.data.model.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TripDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertTrip(Trips trips);

    @Query("SELECT * FROM trip_table WHERE status = 'upComing' AND `user-email` = :userEmail")
    LiveData<List<Trips>> getUpComingTrips(String userEmail);

    @Query("SELECT * FROM trip_table WHERE status NOT LIKE '%upComing%' AND status NOT LIKE '%delete%' AND  `user-email` = :userEmail ")
    LiveData<List<Trips>> getHistoryTrips(String userEmail);

    @Query("SELECT * FROM trip_table WHERE status NOT LIKE '%delete%' AND  `user-email` = :userEmail ")
    LiveData<List<Trips>> getTrips(String userEmail);

    @Query("SELECT * FROM trip_table WHERE tripe_id = :tripId")
    LiveData<Trips> getTripById(int tripId);



    @Query("SELECT notes FROM trip_table WHERE tripe_id = :triId")
    LiveData<List<Note>> getNotes(int triId);

    @Query("UPDATE trip_table SET status = :status WHERE tripe_id = :tripId")
    void updateTripStatus(String status, int tripId);

    @Query("UPDATE trip_table SET tripe_name= :tripName,start_point= :startPoint, end_point= :endPoint, date= :date,time= :time,repeated= :repeated,direction= :direction,`work-manager-tag`= :workManagerTag WHERE tripe_id = :tripId")
    void updateTrip(String tripName, String startPoint,String endPoint, String date,String time,String repeated,boolean direction,String workManagerTag,int tripId);


    @Query("UPDATE trip_table SET notes = :note WHERE tripe_id = :tripId")
    void setNote(List<Note> note, int tripId);


    @Query("SELECT `work-manager-tag` FROM trip_table  WHERE `user-email`=:userEmail AND tripe_id= :tripTd")
    LiveData<String> getWorkManagerTag(String userEmail,int tripTd);

    @Query("DELETE FROM trip_table WHERE tripe_id = :tripTd")
    void deleteTrip(int tripTd);

    @Query("SELECT tripe_id FROM trip_table ORDER BY tripe_id DESC LIMIT 1")
    LiveData<Integer> getTripId();


}
