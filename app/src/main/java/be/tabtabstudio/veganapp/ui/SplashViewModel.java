package be.tabtabstudio.veganapp.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import be.tabtabstudio.veganapp.data.VegRepository;
import be.tabtabstudio.veganapp.data.entities.Location;
import be.tabtabstudio.veganapp.data.entities.User;

public class SplashViewModel extends ViewModel {
    public LiveData<Location> getLocationObservable() { return VegRepository.getInstance().getLocation(); }
    public LiveData<User> getUserObservable() { return VegRepository.getInstance().getUser(); }

    public void setLocation(float lat, float lng) {
        VegRepository.getInstance().setLocation(new Location(lat, lng));
    }

}
