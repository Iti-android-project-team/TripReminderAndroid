package com.example.tripreminder.data.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.tripreminder.ui.activities.dialog.DialogActivity;

public class DialogReceiver extends BroadcastReceiver {
    private final String CHANNEL_ID = "1";
    private int tripId;
    public DialogReceiver(int tripId){
        this.tripId = tripId;

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("work-receive", String.valueOf(tripId));
        Intent intent1 = new Intent(context, DialogActivity.class);
        intent1.putExtra("tripId",tripId);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);

    }


}
