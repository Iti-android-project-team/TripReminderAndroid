package com.example.tripreminder.ui.fragment.history;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.tripreminder.ui.activities.addTrip.AddTripViewModel;

public class HistoryViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String mParam;


    public HistoryViewModelFactory(Application application, String param) {
        mApplication = application;
        mParam = param;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new HistoryViewModel(mApplication, mParam);
    }
}
