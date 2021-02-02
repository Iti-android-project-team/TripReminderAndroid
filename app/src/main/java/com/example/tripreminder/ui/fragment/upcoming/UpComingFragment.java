package com.example.tripreminder.ui.fragment.upcoming;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tripreminder.R;
import com.example.tripreminder.helper.Dialog;
import com.example.tripreminder.model.db.Note;
import com.example.tripreminder.model.db.Notes;
import com.example.tripreminder.model.db.Trips;
import com.example.tripreminder.ui.activities.FloatingViewService;
import com.example.tripreminder.ui.activities.ReminderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UpComingFragment extends Fragment implements Dialog.DialogListener{

    private UpComingViewModel upComingViewModel;
    private static final int RESULT_OK = -1;
    FloatingViewService floatingViewService;
    boolean isBound;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_up_coming,container,false);

        //upComingViewModel = new ViewModelProvider(this).get(UpComingViewModel.class);
upComingViewModel = new ViewModelProvider(this,
        new ViewModelProvider.AndroidViewModelFactory(Objects.requireNonNull(getActivity()).getApplication())).get(UpComingViewModel.class);
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
        nn.setNote("rrrrr ");
        n.add(nn);
        n.add(nn);
        n.add(nn);
        n.add(nn);
//        List<Notes> notes = new ArrayList<>();
//        notes.add(n);

        //upComingViewModel.insert(trips);
       // upComingViewModel.insertNote(n,2);

        //upComingViewModel.updateTrip("upComing",1);

        upComingViewModel.getAllTrips().observe(getViewLifecycleOwner(), it -> {
            if (it.size() != 0) {
                if(it != null){
                    List<Trips> t= it;
                    for(int i = 0 ; i< t.size(); i++){
                 Log.i("Data",t.get(i).getTime());
                 Log.i("Data",t.get(i).getTid()+"");
                        Log.i("Data",t.get(i).getNotes()+"");
                        if(t.get(i).getNotes() != null){
                            for (Note note:t.get(i).getNotes()) {
                                Log.i("Data",note.getNote());
                            }
                        }

                    }
                }

            }

        });
        return view;
    }
    public void openDialog(){
        Dialog dialog = new Dialog(this);
        dialog.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onGoClicked() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getContext())) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getActivity().getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            initializeView();
        }
    }

    @Override
    public void onCancelClicked() {
        Toast.makeText(getContext(),
                "Trip is cancelled",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSnoozeClicked()  {
        createNotificationChannel(getContext());
    }
    private ServiceConnection myConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ReminderService.MyLocalBinder binder = (ReminderService.MyLocalBinder) service;
//            reminderService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };
    private void initializeView() {
        getActivity().startService(new Intent(getActivity(), FloatingViewService.class));
        getActivity().getFragmentManager().popBackStack();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            if (resultCode == RESULT_OK) {
                initializeView();
            } else {
                Toast.makeText(getContext(),
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();
                getActivity().getFragmentManager().popBackStack();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void createNotificationChannel(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel2", "myChannel", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel2")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Trip reminder")
                .setContentText("Time is here!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true);
        notificationManager.notify(15, builder.build());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().stopService(new Intent(getActivity(),FloatingViewService.class));
    }
}
