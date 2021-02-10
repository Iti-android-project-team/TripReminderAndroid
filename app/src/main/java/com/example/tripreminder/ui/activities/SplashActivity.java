package com.example.tripreminder.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.example.tripreminder.R;
import com.example.tripreminder.data.local.SharedPref;

import de.hdodenhof.circleimageview.CircleImageView;

public class SplashActivity extends AppCompatActivity {
    private Animation textAnimation, layoutAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();

    }

    private void init(){
        CircleImageView imageView =findViewById(R.id.logoo);
        textAnimation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.bottom_to_top);
        imageView.setVisibility(View.VISIBLE);
        imageView.setAnimation(textAnimation);
        // Wait for 3 seconds then open home.
        (new Handler()).postDelayed(() -> {
            // Open home.
            imageView.setVisibility(View.VISIBLE);
            imageView.setAnimation(textAnimation);
            SharedPref.createPrefObject(SplashActivity.this);
            boolean isLogin =SharedPref.checkLogin();
         //
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