package com.example.tripreminder.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.tripreminder.R;
import com.example.tripreminder.helper.ViewPagerAdapter;
import com.example.tripreminder.ui.fragment.ProfileFragment;
import com.example.tripreminder.ui.fragment.TripsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private int oldItemId;
    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){
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

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        if (oldItemId != item.getItemId()){
            oldItemId = item.getItemId();

            switch (item.getItemId()) {
                case R.id.nav_tripe:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.nave_profile:
                    viewPager.setCurrentItem(1);
                    return true;
            }
        }

        return  false;
    };


}