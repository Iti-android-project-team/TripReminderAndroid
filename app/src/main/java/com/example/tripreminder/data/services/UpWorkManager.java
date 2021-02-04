package com.example.tripreminder.data.services;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.work.Worker;
import androidx.work.WorkerParameters;



public class UpWorkManager extends Worker {
    Context mcontext;
    private DialogReceiver myBroadcast;
    public UpWorkManager(@NonNull  Context context,@NonNull  WorkerParameters parameters){
        super(context,parameters);
        this.mcontext = context;
    }
    @NonNull
    @Override
    public Result doWork() {
        openDialog();
        return Result.success();
    }

    private void openDialog(){
        Log.i("UpWorkManager","hi from work manager");
        IntentFilter intentFilter = new IntentFilter("com.wesam.services.service.ImageReceiver");
        myBroadcast =  new DialogReceiver();
        mcontext.registerReceiver(myBroadcast,intentFilter);
        Intent outIntent = new Intent();
        outIntent.setAction("com.wesam.services.service.ImageReceiver");
        outIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        mcontext.sendBroadcast(outIntent);

    }
}
