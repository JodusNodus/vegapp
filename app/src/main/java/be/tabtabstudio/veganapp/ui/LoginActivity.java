package be.tabtabstudio.veganapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import be.tabtabstudio.veganapp.R;
import be.tabtabstudio.veganapp.data.entities.Location;
import be.tabtabstudio.veganapp.data.entities.User;

public class LoginActivity extends AppCompatActivity {

    private EditText mFirstname;
    private EditText mLastname;
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mAlreadyRegisteredBtn;

    private LoginViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirstname = findViewById(R.id.first_name);
        mLastname = findViewById(R.id.last_name);
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mAlreadyRegisteredBtn = findViewById(R.id.switch_to_login_btn);

        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        mViewModel.setContext(this);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    attemptLogin();
                }
                return false;
            }
        });

        Button mSignUpButton = findViewById(R.id.sign_up_btn);
        mSignUpButton.setOnClickListener((View v) -> {
            attemptSignup();
        });

        mAlreadyRegisteredBtn.setOnClickListener((View v) -> {
            hideSignupFields();
        });

        Button mLoginButton = findViewById(R.id.login_btn);
        mLoginButton.setOnClickListener((View v) -> {
            attemptLogin();
        });

        mViewModel.getUserObservable().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                mViewModel.handleUserLoad(user);
            }
        });
    }

    private void hideSignupFields() {
        mFirstname.setVisibility(EditText.GONE);
        mLastname.setVisibility(EditText.GONE);
        mAlreadyRegisteredBtn.setVisibility(View.GONE);
    }

    private void attemptSignup() {
        mFirstname.setError(null);
        mLastname.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String firstname = mFirstname.getText().toString();
        String lastname = mLastname.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        mViewModel.attempSignup(firstname, lastname, email, password);
    }

    private void attemptLogin() {
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String firstname = mFirstname.getText().toString();
        String lastname = mLastname.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        mViewModel.attempLogin(email, password);
    }
}

