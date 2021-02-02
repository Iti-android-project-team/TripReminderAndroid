package com.example.tripreminder.ui.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;

import com.example.tripreminder.R;

public class ReminderService extends JobIntentService {
    IntentFilter filter = new IntentFilter("com.example.Broadcast");
//    BCReceiver receiver = new BCReceiver();
    private final IBinder binder = new MyLocalBinder();

    public class MyLocalBinder extends Binder {
        public ReminderService getService() {
            return ReminderService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    public ReminderService() {
        super();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(receiver);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        //        it shouldn't register the receiver in on create it only should register when the user click on snooze
//        registerReceiver(receiver, filter);
        Intent intent = new Intent();
        intent.setAction("com.example.Broadcast");
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(intent);
    }

    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
