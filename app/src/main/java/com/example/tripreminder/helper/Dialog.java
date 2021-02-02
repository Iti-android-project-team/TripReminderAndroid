package com.example.tripreminder.helper;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.net.sip.SipSession;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.telecom.ConnectionService;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

import com.example.tripreminder.ui.activities.MainActivity;
import com.example.tripreminder.ui.activities.ReminderService;
import com.example.tripreminder.ui.fragment.UpComingFragment;

public class Dialog extends AppCompatDialogFragment {

    private DialogListener dialogListener;
    private Fragment parent;

    public Dialog(Fragment parent) {
        this.parent = parent;
    }


    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Trip reminder")
                .setMessage("It's your trip time ")
                .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogListener.onGoClicked();
//                        it opens the Map + the floating window with the notes added to the trip
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        close the dialog + removing the trip from upcoming and adding it with canceled status to history trips
                        dialogListener.onCancelClicked();

                    }
                })
                .setNeutralButton("Snooze", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        try {
                            dialogListener.onSnoozeClicked();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    }
                });
        return builder.create();

    }


    public interface DialogListener{
        void onGoClicked();
        void onCancelClicked();
        void onSnoozeClicked() throws InterruptedException;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
            dialogListener = (UpComingFragment) parent;
    }
}
