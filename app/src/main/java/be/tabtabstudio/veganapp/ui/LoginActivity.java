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

    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private LoginViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    attemptLogin();
                }
                return false;
            }
        });

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener((View v) -> {
            attemptLogin();
        });

        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        mViewModel.getUserObservable().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                handleUserLoad(user);
            }
        });

        // Fast testing
        mEmailView.setText("bob@debouwer.com");
        mPasswordView.setText("deusvult");

    }

    private void handleUserLoad(@Nullable User user) {
        if (user != null) {
            Toast.makeText(LoginActivity.this, "Login Succesfull", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void attemptLogin() {
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        mViewModel.attempLogin(email, password);
    }
}

