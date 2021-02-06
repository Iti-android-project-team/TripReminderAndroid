package com.example.tripreminder.ui.activities.addTrip;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.tripreminder.ui.fragment.upcoming.UpComingViewModel;

class AddTripViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String mParam;


    public AddTripViewModelFactory(Application application, String param) {
        mApplication = application;
        mParam = param;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new AddTripViewModel(mApplication, mParam);
    }

}
