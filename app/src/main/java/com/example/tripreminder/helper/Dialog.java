package com.example.tripreminder.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.media.Ringtone;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

import com.example.tripreminder.data.model.db.Trips;
import com.example.tripreminder.ui.fragment.upcoming.UpComingFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;

//public class Dialog extends AppCompatDialogFragment {
//
//    private DialogListener dialogListener;
//    private Fragment parent;
//    private Context context;
//
//    public Dialog(Fragment parent) {
//        this.parent = parent;
//    }
//    public Dialog(Context context) {
//        this.context = context;
//    }
//
//
//    @NonNull
//    @Override
//    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//
//
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Trip reminder")
//                .setMessage("It's your trip time ")
//                .setPositiveButton("Go", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialogListener.onGoClicked();
////                        it opens the Map + the floating window with the notes added to the trip
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        close the dialog + removing the trip from upcoming and adding it with canceled status to history trips
//                        dialogListener.onCancelClicked();
//
//                    }
//                })
//                .setNeutralButton("Snooze", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//
//                        try {
//                            dialogListener.onSnoozeClicked();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                });
//        return builder.create();
//
//    }
//
//
//    public interface DialogListener{
//        void onGoClicked();
//        void onCancelClicked();
//        void onSnoozeClicked() throws InterruptedException;
//    }
//
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//            dialogListener = (UpComingFragment) parent;
//    }
//}
public class Dialog extends AppCompatActivity  {

    Ringtone ringtone;
    Context context;
    Trips trip;
    String id;
//    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_dialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        context = this;
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("TripID");
        Log.i("", "onCreate: " + id);



                    final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("trip.getTripName() + ");
                    alert.setMessage("checking");
                    alert.setCancelable(false);
                    //play();

                    alert.create();
                    alert.show();
                }



    }


