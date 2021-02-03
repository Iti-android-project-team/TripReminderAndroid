package com.example.tripreminder.data.services;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import com.example.tripreminder.helper.Dialog;
import com.example.tripreminder.ui.activities.MainActivity;
import com.example.tripreminder.ui.activities.MainActivity2;

public class DialogReceiver extends BroadcastReceiver {
    private final String CHANNEL_ID = "1";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("ImageReceiver","onReceive");
        Intent intent1 = new Intent(context, MainActivity2.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);

    }


}