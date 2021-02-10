package com.example.tripreminder.ui.fragment.upcoming;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.provider.Settings;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.tripreminder.data.local.SharedPref;
import com.example.tripreminder.data.services.DialogReceiver;
import com.example.tripreminder.ui.activities.dialog.DialogActivity;
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
import com.google.protobuf.StringValue;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.ALARM_SERVICE;

public class UpComingFragment extends Fragment implements UPComingAdapter.OnItemClickListener {
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
    private String userEmail;


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
        deleteItemBySwabbing();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!Settings.canDrawOverlays(getContext())) {
//                getPermission();
//            }
//        }
        return view;
    }

    private void deleteItemBySwabbing() {
        // Delete subject by swabbing item left and right
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(90, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
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
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    public void openDialog(Context context, Trips trip) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Are you sure delete trip " + trip.getTripName() + " ? ");
        builder1.setCancelable(false);
        builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                upComingViewModel.updateTrip("delete", trip.getTripId());
                cancelWorkManager(userEmail, trip.getTripId());
                //adapter.setTrips(tripList);
            }
        });

        builder1.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.setTrips(tripList);
                dialog.dismiss();
            }
        });

        builder1.create();
        builder1.show();

    }

    public void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getContext())) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + requireActivity().getPackageName()));
            startActivityForResult(intent, 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(getContext())) {
                    Toast.makeText(getContext(), "permission denied by user.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void init(View view) {

        tripList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.upComing_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new UPComingAdapter(getContext(), this);
        recyclerView.setAdapter(adapter);

        SharedPref.createPrefObject(getContext());
        userEmail = SharedPref.getUserEmail();

        Log.e("len", userEmail);
        if (!userEmail.equals(" ")) {
            upComingViewModel = new ViewModelProvider(this, new MyViewModelFactory(getActivity().getApplication(),
                    userEmail)).get(UpComingViewModel.class);
            getAllTrips();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().stopService(new Intent(getActivity(), FloatingViewService.class));
    }

    @Override
    public void onItemNoteClick(int position) {

        SharedPref.setNotes(new Gson().toJson(tripList.get(position).getNotes()));
        Intent intent = new Intent(getContext(), AddNoteActivity.class);
        int tripId = tripList.get(position).getTripId();
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
        intent.putExtra("WORK_MANGER_TAG",tripList.get(position).getWorkManagerTag());

        startActivity(intent);

    }

    private void initializeView(int position) {
        Log.i("len", "initialize");
        Intent intent = new Intent(getContext(), FloatingViewService.class);
//        intent.putExtra("tripId",new Trips().getTripId());
        if (tripList.get(position).getNotes() != null) {
            SharedPref.setFloatingNotes(new Gson().toJson(tripList.get(position).getNotes()));
            Log.i("tripList notes", (tripList.get(position).getNotes()).toString());
        }
        getActivity().startService(intent);
    }

    @Override
    public void onItemStartClick(int position) {
        Log.i("Data", "onItemClickListener");
        int tripId = tripList.get(position).getTripId();
        String editTripEnd = tripList.get(position).getEndPoint();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!Settings.canDrawOverlays(getContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getContext().getPackageName()));
                startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
            } else {
                initializeView(position);
            }
        } else {
            Toast.makeText(getContext(), "Your android version does not support this service", Toast.LENGTH_LONG).show();
        }
        upComingViewModel.updateTrip("Done", tripId);
        cancelWorkManager(userEmail, tripList.get(position).getTripId());
        tripList.remove(position);
        adapter.setTrips(tripList);
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + editTripEnd);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }


//

    private void cancelWorkManager(String userEmail, int tripId) {
        upComingViewModel.getWorkManageTag(userEmail, tripId).observe(getViewLifecycleOwner(), it -> {
            Log.i("workManagerTag", it);
            if (it != null) {
                upComingViewModel.cancelWorkManager(it);
            }
        });
    }

    private void getAllTrips() {
        upComingViewModel.getAllTrips().observe(getViewLifecycleOwner(), it -> {

            if (it.size() > 0) {
                Log.i("data getAllTrips", String.valueOf(it.size()));
                if (it != null) {
                    List<Trips> t = it;
                    tripList = t;
                    adapter.setTrips(tripList);
                }
            }else{

            }

        });
    }
}

