package com.example.tripreminder.ui.fragment.upcoming;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tripreminder.R;
import com.example.tripreminder.model.db.Trips;

import java.util.Objects;

public class UpComingFragment extends Fragment {

    private UpComingViewModel upComingViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trips,container,false);

        //upComingViewModel = new ViewModelProvider(this).get(UpComingViewModel.class);
upComingViewModel = new ViewModelProvider(this,
        new ViewModelProvider.AndroidViewModelFactory(Objects.requireNonNull(getActivity()).getApplication())).get(UpComingViewModel.class);
        Trips trips = new Trips();
        trips.setTripName("Test");
        trips.setDate("Test");
        trips.setStatus("Test");
        trips.setDirection(true);
        trips.setEndPoint("Test");
        trips.setRepeated("Test");
        trips.setTime("Test");
        trips.setStartPoint("Test");
        upComingViewModel.insert(trips);
        upComingViewModel.getAllTrips().observe(getViewLifecycleOwner(), it -> {
            if (it.size() > 0) {
               Log.i("Data",it.toString());
            }

        });
        return view;
    }
}
