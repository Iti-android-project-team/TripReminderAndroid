package com.example.tripreminder.ui.fragment.history;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.tripreminder.ui.activities.MapsFragment;
import com.example.tripreminder.R;
import com.example.tripreminder.adapter.HistoryAdapter;
import com.example.tripreminder.data.local.SharedPref;
import com.example.tripreminder.data.model.db.Trips;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;



public class HistoryFragment extends Fragment implements HistoryAdapter.OnItemClickListener {

    private RecyclerView historyRV;
    private HistoryAdapter adapter;

    private HistoryViewModel historyViewModel;
    private List<Trips> historyList = new ArrayList<>();

    private ImageView btnShowTrips;
    ProgressDialog progressDialog;

    private LottieAnimationView emptyList;
    private TextView txtEmptyList;

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

    private void init(View view) {
        historyRV = view.findViewById(R.id.historyRecyclerView);
        historyRV.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HistoryAdapter(this, getContext());
        historyRV.setAdapter(adapter);
        btnShowTrips = view.findViewById(R.id.btnShowTrips);

        emptyList = view.findViewById(R.id.empty_list);
        txtEmptyList = view.findViewById(R.id.txt_empty);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait until map loaded...");

        SharedPref.createPrefObject(getContext());
        String userEmail = SharedPref.getUserEmail();
        Log.e("len", userEmail);

        if (!userEmail.equals(" ")) {
            historyViewModel = new ViewModelProvider(this, new HistoryViewModelFactory(getActivity().getApplication(),
                    userEmail)).get(HistoryViewModel.class);
            getAllHistoryTrips();
        }

        buttonClickedListeners();
        deleteItemBySwabbing();
    }

    private void deleteItemBySwabbing() {
        // Delete subject by swabbing item left and right
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(90,
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                final Trips trip = adapter.getItem(position);
                assert trip != null;
                openDialog(getContext(), trip);
            }
        });
        itemTouchHelper.attachToRecyclerView(historyRV);
    }

    @Override
    public void showNotesButtonClicked(int position) {
        Log.i("position", String.valueOf(position));
        if (historyList.get(position).getNotes() != null) {
            SharedPref.setNotes(new Gson().toJson(historyList.get(position).getNotes()));
            NoteFragment noteFragment = new NoteFragment();
            noteFragment.show(getParentFragmentManager(), "note-dialog");
        } else {
            Toast.makeText(getContext(), "This item doesn't have any notes", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void deleteTripButtonClicked(int position) {
        final Trips trip = adapter.getItem(position);
        assert trip != null;
        openDialog(getContext(), trip);
    }


    public void openDialog(Context context, Trips trip) {
        //int tripId = historyList.get(position).getTripId();
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Are you sure delete trip " + trip.getTripName() + " ? ");
        builder1.setCancelable(false);
        builder1.setPositiveButton("Ok", (dialog, which) -> historyViewModel.deleteTrip("delete", trip.getTripId()));

        builder1.setNegativeButton("CANCEL", (dialog, which) -> {
            adapter.loadData(historyList);
            dialog.dismiss();
        });

        builder1.create();
        builder1.show();
    }

    private void getAllHistoryTrips() {
        historyViewModel.getAllHistory().observe(getViewLifecycleOwner(), it -> {
            Log.i("size", String.valueOf(it.size()));
            if (it.size() > 0) {
                historyRV.setVisibility(View.VISIBLE);
                emptyList.setVisibility(View.GONE);
                txtEmptyList.setVisibility(View.GONE);
                btnShowTrips.setVisibility(View.VISIBLE);
                Log.i("data", String.valueOf(it.size()));
                if (it != null) {
                    historyList = it;
                    adapter.loadData(historyList);
                }
            } else {
                historyRV.setVisibility(View.GONE);
                emptyList.setVisibility(View.VISIBLE);
                txtEmptyList.setVisibility(View.VISIBLE);
                btnShowTrips.setVisibility(View.GONE);
                //historyList.clear();
            }

        });
    }

    private void buttonClickedListeners() {
        btnShowTrips.setOnClickListener(v -> {
            progressDialog.show();
            //getAddress();
            openMap();
        });
    }

    private void openMap() {
        if (historyList.size() > 0 && !historyList.isEmpty()) {
            if(isNetworkConnected()){
                Intent startMap = new Intent(getContext(), MapsFragment.class);
                startMap.putExtra("HISTORY_TRIPS", (new Gson().toJson(historyList)));
                startActivity(startMap);
            }else{
                progressDialog.dismiss();
                Toast.makeText(getContext(), "connection issue please check you connection", Toast.LENGTH_SHORT).show();
            }

        } else {
            progressDialog.dismiss();
            Toast.makeText(getContext(), "There is  no trips to show", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        progressDialog.dismiss();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}