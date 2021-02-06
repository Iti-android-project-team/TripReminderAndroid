package com.example.tripreminder.ui.activities.dialog;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DialogViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String mParam;


    public DialogViewModelFactory(Application application, String param) {
        mApplication = application;
        mParam = param;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new DialogViewModel(mApplication, mParam);
    }

}
