package com.example.tripreminder.ui.fragment.upcoming;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tripreminder.R;
import com.example.tripreminder.data.local.SharedPref;
import com.example.tripreminder.data.model.db.Note;
import com.example.tripreminder.data.model.db.Trips;

import com.example.tripreminder.helper.MyViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class UpComingFragment extends Fragment{

    private UpComingViewModel upComingViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // openDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_up_coming, container, false);


        //upComingViewModel = new ViewModelProvider(this).get(UpComingViewModel.class);
        String userEmail = SharedPref.getUserEmail();
        Log.e("len", userEmail);
        if (!userEmail.equals(" ")) {
            upComingViewModel = new ViewModelProvider(this, new MyViewModelFactory(getActivity().getApplication(),
                    userEmail)).get(UpComingViewModel.class);
        }

        Trips trips = new Trips();
        trips.setTripName("test2");
        trips.setDate("hi");
        trips.setStatus("upComing");
        trips.setDirection(false);
        trips.setEndPoint("hi");
        trips.setRepeated("hi");
        trips.setTime("hi");
        trips.setStartPoint("hi");
        trips.setUserEmail(userEmail);

        List<Note> n = new ArrayList<>();
        Note nn = new Note();
        nn.setNote("rrrrr ");
        n.add(nn);
        n.add(nn);
        n.add(nn);
        n.add(nn);


        upComingViewModel.insert(trips);
        //upComingViewModel.updateTrip("done", 16);
        //upComingViewModel.deleteTrip(1);

        upComingViewModel.getAllTrips().observe(getViewLifecycleOwner(), it -> {
            if (it.size() != 0) {
                Log.i("length", String.valueOf(it.size()));
                if (it != null) {
                    List<Trips> t = it;
                    for (int i = 0; i < t.size(); i++) {
                        Log.i("Data", t.get(i).getTime());
                        Log.i("Data", t.get(i).getTid() + "");
                        Log.i("Data", t.get(i).getStatus()+ "");
                        if (t.get(i).getNotes() != null) {
                            for (Note note : t.get(i).getNotes()) {
                                Log.i("Data", note.getNote());
                            }
                        }

                    }
                }
            }
        });

        upComingViewModel.getAllHistory().observe(getViewLifecycleOwner(), it -> {
            if (it.size() != 0) {
                Log.i("lengthHistory", String.valueOf(it.size()));
                if (it != null) {
                    List<Trips> t = it;
                    for (int i = 0; i < t.size(); i++) {
                        Log.i("Data", t.get(i).getTime());
                        Log.i("Data", t.get(i).getTid() + "");
                        Log.i("Data", t.get(i).getNotes() + "");
                        if (t.get(i).getNotes() != null) {
                            for (Note note : t.get(i).getNotes()) {
                                Log.i("Data", note.getNote());
                            }
                        }

                    }
                }
            }
        });
        return view;
    }


}
