package com.example.tripreminder.ui.fragment.upcoming;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tripreminder.R;
import com.example.tripreminder.adapter.UPComingAdapter;
import com.example.tripreminder.data.model.db.Trips;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class UpComingFragment extends Fragment {
    //implements Dialog.DialogListener{

    private UPComingAdapter adapter;
    private List<Trips> tripList;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private UpComingViewModel upComingViewModel;
//9cb60fdb631b241bd7da47637c0ffa43bf18ff0a


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

        upComingViewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(Objects.requireNonNull(getActivity()).getApplication())).get(UpComingViewModel.class);
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
