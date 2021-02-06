package com.example.tripreminder.ui.activities.addNote;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.tripreminder.ui.activities.addTrip.AddTripViewModel;

public class AddNoteViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String mParam;


    public AddNoteViewModelFactory(Application application, String param) {
        mApplication = application;
        mParam = param;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new AddNoteViewModel(mApplication, mParam);
    }
}
