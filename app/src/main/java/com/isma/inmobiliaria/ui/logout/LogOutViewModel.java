package com.isma.inmobiliaria.ui.logout;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.isma.inmobiliaria.request.ApiClient;

public class LogOutViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> mCerrarSesion = new MutableLiveData<>(false);

    public LiveData<Boolean> getCerrarSesion() {
        if (mCerrarSesion == null) {
            mCerrarSesion = new MutableLiveData<>();
        }
        return mCerrarSesion;
    }
    public LogOutViewModel(@NonNull Application application) {
        super(application);
    }

    public void cerrarSesion() {
        ApiClient.borrarToken(getApplication());
        mCerrarSesion.postValue(true);
    }
    // TODO: Implement the ViewModel
}