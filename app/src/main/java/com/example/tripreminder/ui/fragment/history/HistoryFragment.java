package com.example.tripreminder.ui.fragment.history;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tripreminder.MapsFragment;
import com.example.tripreminder.R;
import com.example.tripreminder.adapter.HistoryAdapter;
import com.example.tripreminder.data.local.SharedPref;
import com.example.tripreminder.data.model.db.Trips;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment implements HistoryAdapter.OnItemClickListener {

    private RecyclerView historyRV;
    private HistoryAdapter adapter;

    private HistoryViewModel historyViewModel;
    private List<Trips> historyList = new ArrayList<>();
    private List<Double>  latitudeStartPointList = new ArrayList<>();
    private List<Double>  longitudeStartPointList = new ArrayList<>();
    private List<Double>  latitudeEndPointList = new ArrayList<>();
    private List<Double>  longitudeEndPointList = new ArrayList<>();

    private ImageView btnShowTrips;
    ProgressDialog progressDialog;


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

    private void init(View view) {
        historyRV = view.findViewById(R.id.historyRecyclerView);
        historyRV.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HistoryAdapter(this, getContext());
        historyRV.setAdapter(adapter);
        btnShowTrips = view.findViewById(R.id.btnShowTrips);

        progressDialog=new ProgressDialog(getContext());
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
//          Log.i("position", String.valueOf(historyList.get(position).getNotes()));
//          for(Note i : historyList.get(position).getNotes()){
//              Log.i("position", i.getNote());
//          }
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

        builder1.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.loadData(historyList);
                dialog.dismiss();
            }
        });

        builder1.create();
        builder1.show();
    }

    private void getAllHistoryTrips(){
        historyViewModel.getAllHistory().observe(getViewLifecycleOwner(), it -> {
            Log.i("size", String.valueOf(it.size()));
            if (it.size() > 0) {
                Log.i("data", String.valueOf(it.size()));
                if (it != null) {
                    List<Trips> t = it;
                    historyList = t;
                    adapter.loadData(historyList);
                }
            } else {

            }

        });
    }
    private void getAddress(){
        Log.i("History getLatLog", String.valueOf(historyList.size()));
        if(historyList.size() > 0){
            for(int i = 0 ;i<historyList.size();i++){
                getLatLong(historyList.get(i).getStartPoint());
                getLatLongEndPoint(historyList.get(i).getEndPoint());
            }
            openMap();
        }else{
            progressDialog.dismiss();
            Toast.makeText(getContext(),"There is  no trips to show",Toast.LENGTH_SHORT).show();
        }

    }

    private void buttonClickedListeners(){
        latitudeStartPointList.clear();
        latitudeEndPointList.clear();
        longitudeStartPointList.clear();
        longitudeEndPointList.clear();
        btnShowTrips.setOnClickListener(v->{
            progressDialog.show();
            getAddress();
        });
    }

    private void openMap(){
        Intent startMap = new Intent(getContext(), MapsFragment.class);
        startMap.putExtra("LATITUDE_START-POINT",(new Gson().toJson(latitudeStartPointList)));
        startMap.putExtra("LONGITUDE_START_POINT",(new Gson().toJson(longitudeStartPointList)));
        startMap.putExtra("LATITUDE_END_POINT",(new Gson().toJson(latitudeEndPointList)));
        startMap.putExtra("LONGITUDE_END_POINT",(new Gson().toJson(longitudeEndPointList)));
        startActivity(startMap);
    }

    private void getLatLong(String address){
        if(Geocoder.isPresent()){
            try {
                String location = address;
                Geocoder gc = new Geocoder(getContext());
                List<Address> addresses= gc.getFromLocationName(location, 5); // get the found Address Objects

                List<LatLng> ll = new ArrayList<LatLng>(addresses.size()); // A list to save the coordinates if they are available
                for(Address a : addresses){
                    if(a.hasLatitude() && a.hasLongitude()){
                        ll.add(new LatLng(a.getLatitude(), a.getLongitude()));
                    }
                }
                if(ll.size()>0){
                    latitudeStartPointList.add(ll.get(0).latitude) ;
                    longitudeStartPointList.add( ll.get(0).longitude);
                    Log.i("IOException", String.valueOf(ll.get(0).latitude));
                    Log.i("IOException", String.valueOf(ll.get(0).longitude));
                }

            } catch (IOException e) {
                // handle the exception
                Log.i("IOException",e.getLocalizedMessage());
            }
        }

    }

    private void getLatLongEndPoint(String address){
        if(Geocoder.isPresent()){
            try {
                String location = address;
                Geocoder gc = new Geocoder(getContext());
                List<Address> addresses= gc.getFromLocationName(location, 5); // get the found Address Objects

                List<LatLng> ll = new ArrayList<LatLng>(addresses.size()); // A list to save the coordinates if they are available
                for(Address a : addresses){
                    if(a.hasLatitude() && a.hasLongitude()){
                        ll.add(new LatLng(a.getLatitude(), a.getLongitude()));
                    }
                }
                if(ll.size()>0){
                    latitudeEndPointList.add(ll.get(0).latitude) ;
                    longitudeEndPointList.add( ll.get(0).longitude);
                    Log.i("IOException", String.valueOf(ll.get(0).latitude));
                    Log.i("IOException", String.valueOf(ll.get(0).longitude));
                }

            } catch (IOException e) {
                // handle the exception
                Log.i("IOException",e.getLocalizedMessage());
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        progressDialog.dismiss();
    }
}