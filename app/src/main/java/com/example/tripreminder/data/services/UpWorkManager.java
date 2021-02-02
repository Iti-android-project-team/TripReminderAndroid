package com.example.tripreminder.data.services;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.tripreminder.helper.Dialog;

public class UpWorkManager extends Worker {

    private final Context context;
    public UpWorkManager(@NonNull  Context context,@NonNull  WorkerParameters parameters){
        super(context,parameters);
        this.context = context;
    }
    @NonNull
    @Override
    public Result doWork() {
        openDialog();
        return Result.success();
    }

    private void openDialog(){
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        Dialog dialog = new Dialog(context);
        dialog.show(fragmentManager,"up-work");
    }
}
