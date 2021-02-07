package com.example.tripreminder.ui.fragment.history;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tripreminder.R;
import com.example.tripreminder.adapter.HistoryAdapter;
import com.example.tripreminder.adapter.UPComingAdapter;
import com.example.tripreminder.data.local.SharedPref;
import com.example.tripreminder.data.model.db.Note;
import com.example.tripreminder.data.model.db.Trips;
import com.example.tripreminder.helper.MyViewModelFactory;
import com.example.tripreminder.ui.fragment.upcoming.UpComingViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class HistoryFragment extends Fragment implements HistoryAdapter.OnItemClickListener {

    private RecyclerView historyRV;
    private HistoryAdapter adapter;

    private HistoryViewModel historyViewModel;
    private List<Trips> historyList;


    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        init(view);


        return view;
    }

    private void  init(View view){
        historyRV = view.findViewById(R.id.historyRecyclerView);
        historyRV.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HistoryAdapter(this,getContext());
        historyRV.setAdapter(adapter);

        SharedPref.createPrefObject(getContext());
        String userEmail = SharedPref.getUserEmail();
        Log.e("len", userEmail);

        if (!userEmail.equals(" ")) {
            historyViewModel = new ViewModelProvider(this, new HistoryViewModelFactory(getActivity().getApplication(),
                    userEmail)).get(HistoryViewModel.class);
        }

        historyViewModel.getAllHistory().observe(getViewLifecycleOwner(), it -> {
            Log.i("size", String.valueOf(it.size()));
            if (it.size() > 0) {
                Log.i("data", String.valueOf(it.size()));
                if (it != null) {
                    List<Trips> t = it;
                    historyRV.setVisibility(View.VISIBLE);
                    historyList = t;
                    adapter.loadData(historyList);
                }
            }else {
                historyRV.setVisibility(View.INVISIBLE);
            }

        });
    }

    @Override
    public void showNotesButtonClicked(int position) {
      Log.i("position", String.valueOf(position));
      if(historyList.get(position).getNotes() != null){
//          Log.i("position", String.valueOf(historyList.get(position).getNotes()));
//          for(Note i : historyList.get(position).getNotes()){
//              Log.i("position", i.getNote());
//          }
          SharedPref.setNotes(new Gson().toJson(historyList.get(position).getNotes()));
          NoteFragment noteFragment = new NoteFragment();
          noteFragment.show(getParentFragmentManager(), "note-dialog");
      }else{
          Toast.makeText(getContext(),"This item doesn't have any notes",Toast.LENGTH_LONG).show();
      }

    }

    @Override
    public void deleteTripButtonClicked(int position) {

       int tripId = historyList.get(position).getTripId();
       Log.i("id", String.valueOf(tripId));
       //historyList.remove(position);
       historyViewModel.deleteTrip("delete",tripId);

    }
}