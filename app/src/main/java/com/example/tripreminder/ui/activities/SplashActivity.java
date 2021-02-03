package com.example.tripreminder.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.tripreminder.R;
import com.example.tripreminder.data.local.SharedPref;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    private void init(){
        // Wait for 3 seconds then open home.
        (new Handler()).postDelayed(() -> {
            // Open home.
            SharedPref.createPrefObject(SplashActivity.this);
            boolean isLogin =SharedPref.checkLogin();

            if(isLogin) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}