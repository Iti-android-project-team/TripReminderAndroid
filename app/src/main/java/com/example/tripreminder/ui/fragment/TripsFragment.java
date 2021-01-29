package com.example.tripreminder.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tripreminder.R;
import com.example.tripreminder.helper.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class TripsFragment extends Fragment {

    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trips,container,false);
        init(view);
        return view;
    }

    private void init(View view){
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tab_layout);
        setAdapters();

    }

    private void setAdapters(){
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        viewPagerAdapter.addFragment(new UpComingFragment());
        viewPagerAdapter.addFragment(new HistoryFragment());
        viewPager.setUserInputEnabled(false);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("UpComing");
                    break;
                case 1:
                    tab.setText("History");
                    break;

            }

        }
        ).attach();

    }
}