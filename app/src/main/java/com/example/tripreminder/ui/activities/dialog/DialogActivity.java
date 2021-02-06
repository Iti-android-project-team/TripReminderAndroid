package com.example.tripreminder.ui.activities.dialog;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.example.tripreminder.R;
import com.example.tripreminder.data.local.SharedPref;
import com.example.tripreminder.ui.activities.FloatingViewService;
import com.example.tripreminder.ui.activities.MainActivity;
import com.example.tripreminder.ui.activities.addTrip.AddTripViewModel;

public class DialogActivity extends AppCompatActivity {
    private static final int RESULT_OK = -1;
    boolean isBound;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    Ringtone ringtone;
   public static String address;
    private String userEmail;
    private DialogViewModel viewModel;
    private int tripId;
    private String endPoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        address = "Suez Canal University";

        init();

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        Bundle bundle = getIntent().getExtras();
//        id = bundle.getString("TripID");
//        Log.i("", "onCreate: " + id);

        final AlertDialog.Builder alert = new AlertDialog.Builder(DialogActivity.this);
        alert.setTitle("Your trip is here!");
        alert.setMessage("your trip time has come");
        alert.setCancelable(false);
        alert.setPositiveButton("Go", new DialogInterface.OnClickListener() {
                     @Override
                    public void onClick(DialogInterface dialog, int which) {
                         onGoClicked();
                         ringtone.stop();
                    }
                 })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onCancelClicked();
                        ringtone.stop();
                    }
                })
                .setNeutralButton("Snooze", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onSnoozeClicked();
                        ringtone.stop();
                    }
                });
                     alert.create();
                     alert.show();
                     play();
    }


        private void createNotificationChannel(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel2", "myChannel", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
            NotificationCompat.Action action;
            Intent intent = new Intent(this, DialogActivity.class);
            intent.putExtra("tripId",tripId);
            intent.putExtra("endPoint",endPoint);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
            action = new NotificationCompat.Action(R.drawable.logo, "end snooze", pendingIntent);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel2")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Trip reminder")
                .setContentText("Time is here!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(action)
                .setOngoing(true);
        notificationManager.notify(15, builder.build());

    }

    public void onGoClicked() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            initializeView();
        }
        viewModel.updateTrip("Done",tripId);
        navigateToMain();
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+address);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void onCancelClicked() {
        Toast.makeText(this, "Trip is cancelled", Toast.LENGTH_LONG).show();
        viewModel.updateTrip("Cancel",tripId);
        navigateToMain();
    }

    public void onSnoozeClicked()  {
        createNotificationChannel(this);
        finish();
    }
    private ServiceConnection myConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.stopService(new Intent(this, FloatingViewService.class));
    }

    private void play() {
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), sound);
        ringtone.play();
    }

    private void initializeView() {
                startService(new Intent(DialogActivity.this, FloatingViewService.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            if (resultCode == RESULT_OK) {
                initializeView();
            } else {
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void navigateToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void init(){
        tripId = getIntent().getIntExtra("tripId",0);
        endPoint = getIntent().getStringExtra("endPoint");
        Log.i("UpWorkManagerId", String.valueOf(tripId));
        Log.i("UpWorkManagerEndPoint", endPoint);
        SharedPref.createPrefObject(DialogActivity.this);
        userEmail = SharedPref.getUserEmail();
        if (!userEmail.equals(" ")) {
            viewModel = new ViewModelProvider(this, new DialogViewModelFactory(getApplication(),
                    userEmail)).get(DialogViewModel.class);
        }
        FloatingViewService.id = tripId;

    }
}