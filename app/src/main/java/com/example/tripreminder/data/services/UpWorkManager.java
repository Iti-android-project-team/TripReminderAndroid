package com.example.tripreminder.data.services;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UpWorkManager extends Worker {

    public UpWorkManager(@NonNull  Context context,@NonNull  WorkerParameters parameters){
        super(context,parameters);
    }
    @NonNull
    @Override
    public Result doWork() {
        openDialog();
        return Result.success();
    }

    private void openDialog(){
        Log.i("UpWorkManager","hi from work manager");

    }
}
