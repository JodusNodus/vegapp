package be.tabtabstudio.veganapp.ui;

import android.arch.lifecycle.LiveData;
import android.widget.Toast;

import java.util.HashMap;

import be.tabtabstudio.veganapp.R;
import be.tabtabstudio.veganapp.data.VegRepository;
import be.tabtabstudio.veganapp.data.entities.User;
import be.tabtabstudio.veganapp.data.network.requestBodies.UserLoginBody;
import be.tabtabstudio.veganapp.data.network.requestBodies.UserSignupBody;

import static android.app.Activity.RESULT_OK;

public class SignupViewModel extends ViewModel {

    public LiveData<User> getUserObservable() {
        return VegRepository.getInstance(getContext()).getUserObservable();
    }

    public void attempSignup(String firstname, String lastname, String email, String password) {
        VegRepository.getInstance(getContext()).signup(new UserSignupBody(firstname, lastname, email, password));
    }

    public HashMap<String, String> validateFields(String firstname, String lastname, String email, String password) {
        HashMap<String, String> errors = new HashMap<>();
        if (firstname.isEmpty() || firstname.length() < 3) {
            errors.put("firstname", getContext().getString(R.string.error_too_short));
        }

        if (lastname.isEmpty() || lastname.length() < 3) {
            errors.put("lastname", getContext().getString(R.string.error_too_short));
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errors.put("email", getContext().getString(R.string.error_invalid_email));
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 30) {
            errors.put("password", getContext().getString(R.string.error_password_wrong_length));
        }
        return errors;
    }
}
