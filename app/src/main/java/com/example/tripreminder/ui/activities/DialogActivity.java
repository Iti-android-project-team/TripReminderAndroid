package com.example.tripreminder.ui.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.tripreminder.R;

public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        Bundle bundle = getIntent().getExtras();
//        id = bundle.getString("TripID");
//        Log.i("", "onCreate: " + id);



        final AlertDialog.Builder alert = new AlertDialog.Builder(DialogActivity.this);
        alert.setTitle("trip.getTripName() + ");
        alert.setMessage("checking");
        alert.setCancelable(false);
        //play();

        alert.create();
        alert.show();
    }
}