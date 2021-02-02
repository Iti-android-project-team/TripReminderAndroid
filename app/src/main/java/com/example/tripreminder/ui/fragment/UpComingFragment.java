package com.example.tripreminder.ui.fragment;

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

import android.os.IBinder;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tripreminder.R;
import com.example.tripreminder.helper.Dialog;
import com.example.tripreminder.ui.activities.FloatingViewService;
import com.example.tripreminder.ui.activities.ReminderService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpComingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpComingFragment extends Fragment implements Dialog.DialogListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int RESULT_OK = -1;
    FloatingViewService floatingViewService;
    boolean isBound;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpComingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpComingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpComingFragment newInstance(String param1, String param2) {
        UpComingFragment fragment = new UpComingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //        it doesn't open in on create it opens at the time of the trip
        openDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_up_coming, container, false);
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
        getActivity().startService(new Intent(getActivity(),FloatingViewService.class));
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