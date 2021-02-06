package com.example.tripreminder.ui.fragment.profile;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.tripreminder.ui.fragment.history.HistoryViewModel;

public class ProfileViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String mParam;


    public ProfileViewModelFactory(Application application, String param) {
        mApplication = application;
        mParam = param;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new ProfileViewModel(mApplication, mParam);
    }
}

