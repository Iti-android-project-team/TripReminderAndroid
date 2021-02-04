package com.example.tripreminder.ui.fragment.upcoming;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tripreminder.R;

import com.example.tripreminder.adapter.UPComingAdapter;
import com.example.tripreminder.data.model.db.Trips;

import com.example.tripreminder.data.local.SharedPref;
import com.example.tripreminder.data.model.db.Note;
import com.example.tripreminder.data.model.db.Trips;

import com.example.tripreminder.helper.MyViewModelFactory;


import java.util.ArrayList;
import java.util.List;

public class UpComingFragment extends Fragment {
    //implements Dialog.DialogListener{


    private UPComingAdapter adapter;
    private List<Trips> tripList;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private UpComingViewModel upComingViewModel;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_up_coming, container, false);
        init(view);

        return view;
    }

    private void init(View view) {
        tripList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.upComing_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SharedPref.createPrefObject(getContext());
        String userEmail = SharedPref.getUserEmail();
        Log.e("len", userEmail);
        //if (!userEmail.equals(" ")) {
            upComingViewModel = new ViewModelProvider(this, new MyViewModelFactory(getActivity().getApplication(),
                    "test@gmail.com")).get(UpComingViewModel.class);
        //}
        upComingViewModel.getAllTrips().observe(getViewLifecycleOwner(), it -> {
            if (it.size() != 0) {
                if (it != null) {
                    List<Trips> t = it;
                    tripList = t;
                    adapter = new UPComingAdapter(getContext(), tripList);
                    recyclerView.setAdapter(adapter);

                }

            }

        });
    }

}
