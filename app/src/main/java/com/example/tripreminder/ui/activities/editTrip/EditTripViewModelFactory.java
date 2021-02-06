package com.example.tripreminder.ui.activities.editTrip;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.tripreminder.ui.activities.addNote.AddNoteViewModel;

public class EditTripViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String mParam;


    public EditTripViewModelFactory(Application application, String param) {
        mApplication = application;
        mParam = param;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new EditTripViewModel(mApplication, mParam);
    }
}
