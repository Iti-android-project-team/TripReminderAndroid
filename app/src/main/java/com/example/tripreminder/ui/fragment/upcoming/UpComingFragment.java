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
import android.widget.Toast;

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


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UpComingFragment extends Fragment implements UPComingAdapter.OnItemClickListener{
        //implements Dialog.DialogListener{
    private UPComingAdapter adapter;
    private  List<Trips>tripList;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private UpComingViewModel upComingViewModel;
    private EditTripViewModel editViewModel;
    DatabaseReference reff;
    private static final int RESULT_OK = -1;
    boolean isBound;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;

    public UpComingFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        openDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_up_coming,container,false);
        tripList =new ArrayList<>();
       recyclerView = view.findViewById(R.id.upComing_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SharedPref.createPrefObject(getContext());

      //  adapter = new UPComingAdapter(getContext(),tripList);
        //recyclerView.setAdapter(adapter);
     /*   String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reff = FirebaseDatabase.getInstance().getReference().child("users").child(currentuser).child("Trips");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    for (DataSnapshot snapshots:snapshot.getChildren()){
                        Trips trip = snapshots.getValue(Trips.class);
                      //  trip.isDirection();
                        String NameTrip = trip.getTripName();
                        String startPointTrip = trip.getStartPoint();
                        String endPointTrip =  trip.getEndPoint();
                        String dateTrip =   trip.getDate();
                        String timeTrip =  trip.getTime();
                        Boolean roundTrip = false;
                        String repeatTrip = trip.getRepeated();
                        String statusTrip = "UpComing";
                        tripList.add(new Trips(NameTrip,startPointTrip,endPointTrip,timeTrip,dateTrip,statusTrip,roundTrip,repeatTrip));
                        adapter = new UPComingAdapter(getContext(),tripList);
                        recyclerView.setAdapter(adapter);
                    }

                }catch (Exception e)
                {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

      // listViewModel.insert(tripList);
     //   adapter.setTrips(tripList);


        //upComingViewModel = new ViewModelProvider(this).get(UpComingViewModel.class);
         upComingViewModel = new ViewModelProvider(this,
        new ViewModelProvider.AndroidViewModelFactory(Objects.requireNonNull(getActivity()).getApplication())).get(UpComingViewModel.class);

        editViewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(Objects.requireNonNull(getActivity()).getApplication())).get(EditTripViewModel.class);

         Trips trips = new Trips();
        trips.setTripName("test");
        trips.setDate("hi");
        trips.setStatus("upComing");
        trips.setDirection(false);
        trips.setEndPoint("hi");
        trips.setRepeated("hi");
        trips.setTime("hi");
        trips.setStartPoint("hi");

        List<Note> n = new ArrayList<>();
        Note nn = new Note();
       nn.setNotes("rrrrr ");
        n.add(nn);
        n.add(nn);
        n.add(nn);
        n.add(nn);
//        List<Notes> notes = new ArrayList<>();
//        notes.add(n);

//        upComingViewModel.insert(trips);
//        upComingViewModel.insertNote(n,2);

        //upComingViewModel.updateTrip("upComing",1);

      upComingViewModel.getAllTrips().observe(getViewLifecycleOwner(), it -> {
            if (it.size() != 0) {
                if(it != null){
                    List<Trips> t= it;
                    tripList = t;
                /*    for(int i = 0 ; i< t.size(); i++){
                        // if(t.get(i).getNotes()!= null) {
                        if (t.get(i).getNotes()!=null) {
                            for (Note note : t.get(i).getNotes()) {
                                Log.e("Data", note.getNote());

                            }
                        }
                    }*/
                    adapter = new UPComingAdapter(getContext(),tripList,this);
                    recyclerView.setAdapter(adapter);


                }

            }

        });

        return view;



    }

//    public void openDialog(){
//        Dialog dialog = new Dialog(this);
//        dialog.show(getFragmentManager(), "dialog");
//    }
//
//    @Override
//    public void onGoClicked() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getContext())) {
//            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                    Uri.parse("package:" + getActivity().getPackageName()));
//            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
//        } else {
//            Intent startMain = new Intent(Intent.ACTION_MAIN);
//            startMain.addCategory(Intent.CATEGORY_HOME);
//            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(startMain);
//            initializeView();
//        }
//    }
//
//    @Override
//    public void onCancelClicked() {
//        Toast.makeText(getContext(),
//                "Trip is cancelled",
//                Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onSnoozeClicked()  {
//        createNotificationChannel(getContext());
//    }
//    private ServiceConnection myConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            isBound = true;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            isBound = false;
//        }
//    };
//    private void initializeView() {
//        getActivity().startService(new Intent(getActivity(), FloatingViewService.class));
//        getActivity().getFragmentManager().popBackStack();
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
//            if (resultCode == RESULT_OK) {
//                initializeView();
//            } else {
//                Toast.makeText(getContext(),
//                        "Draw over other app permission not available. Closing the application",
//                        Toast.LENGTH_SHORT).show();
//                getActivity().getFragmentManager().popBackStack();
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }
//    private void createNotificationChannel(Context context) {
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel("channel2", "myChannel", NotificationManager.IMPORTANCE_HIGH);
//            notificationManager.createNotificationChannel(channel);
//        }
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel2")
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setContentTitle("Trip reminder")
//                .setContentText("Time is here!")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setOngoing(true);
//        notificationManager.notify(15, builder.build());
//
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().stopService(new Intent(getActivity(),FloatingViewService.class));
    }

    @Override
    public void onItemNoteClick(int position) {
        Log.i("Data","onItemClickListener");
/*
       List<Note>note = tripList.get(position).getNotes();
       for (int i = 0 ;i< note.size();i++){
           if (note.get(i).getNote() != null) {
               Log.e("Data", note.get(i).getNote());

           }
       }
          int tripId = tripList.get(position).getTripId();
        Log.e("Data", String.valueOf(tripId));

         Intent intent = new Intent(getContext(), AddNoteActivity.class);
          intent.putExtra("ID",tripId);
        String listNote = new Gson().toJson(note);
        intent.putExtra("Note",listNote);
        Log.e("Data", listNote);

        startActivity(intent);

           List<Note>note = tripList.get(position).getNotes();
            for (int i = 0 ;i< note.size();i++){
            if (note.get(i).getNote() != null) {
                Log.i("Data", note.get(i).getNote());

            }
        }*/

        SharedPref.setNotes(new Gson().toJson(tripList.get(position).getNotes()));

        Intent intent = new Intent(getContext(), AddNoteActivity.class);
        Log.i("id from upcoming", "iddddd");
        int tripId = tripList.get(position).getTripId();
        Log.i("id from upcoming", String.valueOf(tripId));
            intent.putExtra("ID",tripId);
            startActivity(intent);




     /*   Gson gson = new Gson();
        String listNote = new Gson().toJson(note);
        intent.putExtra("Note",listNote);*/


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
        intent.putExtra("EDITID",editTripId);
        intent.putExtra("EDITName",editTripName);
        intent.putExtra("EDITSTART",editTripStart);
        intent.putExtra("EDITEND",editTripEnd);
        intent.putExtra("EDITTIME",editTripTime);
        intent.putExtra("EDITDATE",editTripDate);
        intent.putExtra("EDITROUND",editTripRound);
        intent.putExtra("EDITSPINNER",editTripSpinner);

        startActivity(intent);

    }

    @Override
    public void onItemStartClick(int position) {

    }
/*
    @Override
    public void onItemNoteClick(int position) {
        startActivity(new Intent(getContext(), AddTripActivity.class));
    }

    @Override
    public void onItemEditClick(int position) {

    }

    @Override
    public void onItemStartClick(int position) {

    }*/
}
