package be.tabtabstudio.veganapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import be.tabtabstudio.veganapp.R;
import be.tabtabstudio.veganapp.data.entities.Location;
import be.tabtabstudio.veganapp.data.entities.User;

public class SplashActivity extends AppCompatActivity {

    private static final int USER_LOGIN_CODE = 1719;

    private SplashViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mViewModel = ViewModelProviders.of(this).get(SplashViewModel.class);

        mViewModel.getLocationObservable().observe(this, new Observer<Location>() {
            @Override
            public void onChanged(@Nullable Location location) {
                handleLocationLoad(location);
            }
        });

        loadUser();
    }

    private void loadUser() {
        User user = mViewModel.getUserObservable().getValue();

        if (user == null) {
            Intent k = new Intent(SplashActivity.this, LoginActivity.class);
            startActivityForResult(k, USER_LOGIN_CODE);
        }
    }

    private void handleUserLoad() {
        User user = mViewModel.getUserObservable().getValue();
        Log.i("test", user.firstname);
        loadLocation();
    }

    private void loadLocation() {
        mViewModel.setLocation(51.167788f, 3.264988f);
    }

    private void handleLocationLoad(@Nullable Location location) {
        if (location == null) {
            Toast.makeText(this, "Location set Failed", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Location set Succesfull", Toast.LENGTH_SHORT).show();

        loadProductDetails();
    }

    private void loadProductDetails() {
        Intent k = new Intent(this, ProductDetailsActivity.class);
        startActivity(k);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == USER_LOGIN_CODE && resultCode == RESULT_OK) {
            handleUserLoad();
        }
    }

}
