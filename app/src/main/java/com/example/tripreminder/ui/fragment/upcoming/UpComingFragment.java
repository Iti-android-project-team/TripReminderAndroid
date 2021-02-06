package com.example.tripreminder.ui.fragment.upcoming;

import android.content.Intent;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tripreminder.data.local.SharedPref;
import com.example.tripreminder.ui.activities.editTrip.EditTripActivity;
import com.example.tripreminder.R;

import com.example.tripreminder.adapter.UPComingAdapter;

import com.example.tripreminder.ui.activities.addNote.AddNoteActivity;
import com.example.tripreminder.ui.activities.FloatingViewService;

import com.example.tripreminder.data.model.db.Note;
import com.example.tripreminder.data.model.db.Trips;
import com.example.tripreminder.ui.activities.editTrip.EditTripViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.example.tripreminder.data.model.db.Trips;

import com.example.tripreminder.data.local.SharedPref;
import com.example.tripreminder.data.model.db.Note;
import com.example.tripreminder.data.model.db.Trips;

import com.example.tripreminder.helper.MyViewModelFactory;


import java.util.ArrayList;
import java.util.List;

public class UpComingFragment extends Fragment implements UPComingAdapter.OnItemClickListener {
    //implements Dialog.DialogListener{
    private UPComingAdapter adapter;
    private List<Trips> tripList;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private UpComingViewModel upComingViewModel;
    private EditTripViewModel editViewModel;
    DatabaseReference reff;
    private static final int RESULT_OK = -1;
    boolean isBound;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
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
        if (!userEmail.equals(" ")) {
            upComingViewModel = new ViewModelProvider(this, new MyViewModelFactory(getActivity().getApplication(),
                    userEmail)).get(UpComingViewModel.class);
        }


        upComingViewModel.getAllTrips().observe(getViewLifecycleOwner(), it -> {

            if (it.size() != 0) {
                Log.i("data", String.valueOf(it.size()));
                if (it != null) {
                    List<Trips> t = it;
                    tripList = t;
                    adapter = new UPComingAdapter(getContext(), tripList, this);
                    recyclerView.setAdapter(adapter);

                }
            }

        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().stopService(new Intent(getActivity(), FloatingViewService.class));
    }

    @Override
    public void onItemNoteClick(int position) {
        Log.i("Data", "onItemClickListener");

        SharedPref.setNotes(new Gson().toJson(tripList.get(position).getNotes()));

        Intent intent = new Intent(getContext(), AddNoteActivity.class);
        Log.i("id from upcoming", "iddddd");
        int tripId = tripList.get(position).getTripId();
        Log.i("id from upcoming", String.valueOf(tripId));
        intent.putExtra("ID", tripId);
        startActivity(intent);

    }

    @Override
    public void onItemEditClick(int position) {
        int editTripId = tripList.get(position).getTripId();
        String editTripName = tripList.get(position).getTripName();
        String editTripStart = tripList.get(position).getStartPoint();
        String editTripEnd = tripList.get(position).getEndPoint();
        String editTripTime = tripList.get(position).getTime();
        String editTripDate = tripList.get(position).getDate();
        Boolean editTripRound = tripList.get(position).isDirection();
        String editTripSpinner = tripList.get(position).getRepeated();
        Intent intent = new Intent(getContext(), EditTripActivity.class);
        intent.putExtra("EDITID", editTripId);
        intent.putExtra("EDITName", editTripName);
        intent.putExtra("EDITSTART", editTripStart);
        intent.putExtra("EDITEND", editTripEnd);
        intent.putExtra("EDITTIME", editTripTime);
        intent.putExtra("EDITDATE", editTripDate);
        intent.putExtra("EDITROUND", editTripRound);
        intent.putExtra("EDITSPINNER", editTripSpinner);

        startActivity(intent);

    }

    @Override
    public void onItemStartClick(int position) {

    }
}

