package be.tabtabstudio.veganapp.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import android.util.Log;

import be.tabtabstudio.veganapp.data.VegRepository;
import be.tabtabstudio.veganapp.data.entities.Location;
import be.tabtabstudio.veganapp.data.entities.Product;
import be.tabtabstudio.veganapp.data.entities.User;
import be.tabtabstudio.veganapp.data.network.ApiResponse;
import be.tabtabstudio.veganapp.data.network.requestBodies.UserLoginBody;
import be.tabtabstudio.veganapp.data.network.results.LoginResult;

public class LoginViewModel extends ViewModel {

    public LiveData<User> getUserObservable() {
        return VegRepository.getInstance().getUser();
    }

    public void attempLogin(String email, String password) {
        VegRepository.getInstance().login(email, password);
    }
}
