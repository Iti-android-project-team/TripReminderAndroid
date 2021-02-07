package com.example.tripreminder.ui.fragment.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.tripreminder.R;
import com.example.tripreminder.data.local.SharedPref;
import com.example.tripreminder.data.model.db.Trips;
import com.example.tripreminder.ui.activities.LoginActivity;
import com.example.tripreminder.ui.fragment.history.HistoryViewModel;
import com.example.tripreminder.ui.fragment.history.HistoryViewModelFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private Button logoutButton;
    private Button emailButton;
    private Button syncButton;
    private FirebaseAuth fAuth;
    private FirebaseDatabase fDatabase;
    private String userEmail;
    private ProfileViewModel profileViewModel;
    private List<Trips> tripList = new ArrayList<>();
    private DatabaseReference reference;
    private Map<String, Object> map = new HashMap<>();
    ProgressDialog progressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        logoutButton = view.findViewById(R.id.btn_logout);
        emailButton = view.findViewById(R.id.btn_email);
        syncButton = view.findViewById(R.id.btn_sync);

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Signing In please wait...");

        fAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance();

        SharedPref.createPrefObject(getContext());
        userEmail = SharedPref.getUserEmail();
        if (!userEmail.equals("")) {
            emailButton.setText(SharedPref.getUserEmail());
        }


        Log.e("len", userEmail);

        if (!userEmail.equals(" ")) {
            profileViewModel = new ViewModelProvider(this, new ProfileViewModelFactory(getActivity().getApplication(),
                    userEmail)).get(ProfileViewModel.class);
        }

        getTrips();

        buttonsClicked();
    }

    private void buttonsClicked() {
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref.setLogin(false);
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                //getActivity().getFragmentManager().popBackStack();
                if(!SharedPref.checkLoginWithFirebase()){
                    signOut();
                    LoginActivity.account = null;
                }
            }
        });


        syncButton.setOnClickListener(v -> {
            syncData();
        });
    }

    private void syncData() {
        insertIntoFirebase();
    }

    private void getTrips() {

        profileViewModel.getAllTrips().observe(getViewLifecycleOwner(), it -> {
            Log.i("size", String.valueOf(it.size()));
            if (it.size() > 0) {
                Log.i("data", String.valueOf(it.size()));
                if (it != null) {
                    List<Trips> t = it;
                    tripList = t;
                }
            }

        });
    }

    private void insertIntoFirebase() {
        progressDialog.show();
        if (SharedPref.checkRegisterWithFirebase()) {
            if (tripList != null && tripList.size() > 0) {
                String userID = fAuth.getCurrentUser().getUid();
                reference = fDatabase.getReference("users").child(userID).child("Trips");
                Log.i("userId", userID);
                for (int i = 0; i < tripList.size(); i++) {
                    Log.i("trip", "loop");
                    Log.i("trip", String.valueOf(tripList.get(i).getTid()));
                        map.put("Trip " + tripList.get(i).getTripId(), tripList.get(i));
                        reference.updateChildren(map).addOnCompleteListener(task -> {
                            if(task.isCanceled()){
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "There is issue in sync data please try again later", Toast.LENGTH_SHORT).show();
                            }
                        });
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Data sync successfully", Toast.LENGTH_SHORT).show();
                }

            }else{
                progressDialog.dismiss();
                Toast.makeText(getContext(), "There is no trips to added", Toast.LENGTH_LONG).show();
            }
        } else {
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Sorry your account not found in cloud", Toast.LENGTH_LONG).show();
        }
    }


    private void signOut() {
        LoginActivity.mGoogleSignInClient.signOut();

    }
}

