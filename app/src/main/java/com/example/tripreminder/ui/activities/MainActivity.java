package com.example.tripreminder.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.example.tripreminder.R;
import com.example.tripreminder.helper.ViewPagerAdapter;
import com.example.tripreminder.ui.activities.addTrip.AddTripActivity;
import com.example.tripreminder.ui.fragment.profile.ProfileFragment;
import com.example.tripreminder.ui.fragment.TripsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private int oldItemId;
    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    FloatingActionButton mainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        mainButton = findViewById(R.id.fab);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        oldItemId = R.id.nav_tripe;
        viewPager = findViewById(R.id.pager);
        viewPager.setUserInputEnabled(false);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        viewPagerAdapter.addFragment(new TripsFragment());
        viewPagerAdapter.addFragment(new ProfileFragment());
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        buttonClicked();

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        Log.i("BottomNavigationView", String.valueOf(item.getItemId()));
        Log.i("BottomNavigationView", String.valueOf(oldItemId));
            oldItemId = item.getItemId();

            switch (item.getItemId()) {
                case R.id.nav_tripe:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.nave_profile:
                    viewPager.setCurrentItem(1);
                    return true;
            }

        return  false;
    };


    private void buttonClicked(){
        mainButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddTripActivity.class)));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}