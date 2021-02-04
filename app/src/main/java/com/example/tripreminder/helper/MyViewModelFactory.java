package com.example.tripreminder.helper;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.tripreminder.ui.fragment.upcoming.UpComingViewModel;

public class MyViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String mParam;


    public MyViewModelFactory(Application application, String param) {
        mApplication = application;
        mParam = param;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new UpComingViewModel(mApplication, mParam);
    }
}
