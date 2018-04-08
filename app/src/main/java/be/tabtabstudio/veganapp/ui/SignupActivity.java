package be.tabtabstudio.veganapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import be.tabtabstudio.veganapp.R;
import be.tabtabstudio.veganapp.data.entities.User;
import butterknife.ButterKnife;
import butterknife.BindView;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_first_name) EditText _firstNameText;
    @BindView(R.id.input_last_name) EditText _lastNameText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;
    private SignupViewModel mViewModel;
    private ProgressDialog signupDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);

        mViewModel = ViewModelProviders.of(this).get(SignupViewModel.class);
        mViewModel.setContext(this);

        mViewModel.getUserObservable().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if (user != null) {
                    handleSignupSuccess();
                } else {
                    handleSignupFailed();
                }
            }
        });

        _signupButton.setOnClickListener((view) -> {
            attempSignup();
        });

        _loginLink.setOnClickListener((view) -> {

        });
    }

    public void attempSignup() {
        if (!validate()) {
            handleSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        signupDialog = new ProgressDialog(SignupActivity.this);
        signupDialog.setIndeterminate(true);
        signupDialog.setMessage(getString(R.string.creating_account));
        signupDialog.show();

        String firstname = _firstNameText.getText().toString();
        String lastname = _lastNameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        mViewModel.attempSignup(firstname, lastname, email, password);
    }

    public void handleSignupSuccess() {
        if (signupDialog != null) {
            signupDialog.hide();
        }
        _signupButton.setEnabled(true);
        setResult(RESULT_OK);
        finish();
    }

    public void handleSignupFailed() {
        if (signupDialog != null) {
            signupDialog.hide();
        }
        Toast.makeText(getBaseContext(), getString(R.string.signup_failed), Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        String firstname = _firstNameText.getText().toString();
        String lastname = _lastNameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        HashMap<String, String> errors = mViewModel.validateFields(firstname, lastname, email, password);
        _firstNameText.setError(errors.get("firstname"));
        _lastNameText.setError(errors.get("lastname"));
        _emailText.setError(errors.get("email"));
        _passwordText.setError(errors.get("password"));

        return errors.size() == 0;
    }

    @Override
    public void onBackPressed() {
        // disable going back
        moveTaskToBack(true);
    }

}

