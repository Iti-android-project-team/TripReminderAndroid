package com.example.tripreminder.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tripreminder.R;
import com.example.tripreminder.data.local.SharedPref;
import com.example.tripreminder.ui.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

   private Button logoutButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_profile, container, false);
        init(view);
        return view;
    }

    private void init(View view){
        logoutButton = view.findViewById(R.id.btn_logout);
        SharedPref.createPrefObject(getContext());
        buttonsClicked();
    }

    private void buttonsClicked(){
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref.setLogin(false);
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                //getActivity().getFragmentManager().popBackStack();

            }
        });
    }
}