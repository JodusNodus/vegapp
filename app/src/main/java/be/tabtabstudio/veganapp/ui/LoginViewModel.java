package be.tabtabstudio.veganapp.ui;

import android.arch.lifecycle.LiveData;
import android.widget.Toast;

import be.tabtabstudio.veganapp.data.VegRepository;
import be.tabtabstudio.veganapp.data.entities.User;

import static android.app.Activity.RESULT_OK;

public class LoginViewModel extends be.tabtabstudio.veganapp.ui.ViewModel {

    public LiveData<User> getUserObservable() {
        return VegRepository.getInstance(getContext()).getUserObservable();
    }

    public void attempLogin(String email, String password) {
        VegRepository.getInstance(getContext()).login(email, password);
    }

    public void attempSignup(String firstname, String lastname, String email, String password) {
        VegRepository.getInstance(getContext()).signup(firstname, lastname, email, password);
    }

    public void handleUserLoad(User user) {
        if (user != null) {
            Toast.makeText(getContext(), "Login Succesfull", Toast.LENGTH_SHORT).show();
            ((LoginActivity) getContext()).setResult(RESULT_OK);
            ((LoginActivity) getContext()).finish();
        } else {
            Toast.makeText(getContext(), "Login failed", Toast.LENGTH_SHORT).show();
        }
    }
}
