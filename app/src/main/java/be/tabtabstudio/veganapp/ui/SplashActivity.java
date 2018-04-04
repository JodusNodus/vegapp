package be.tabtabstudio.veganapp.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import be.tabtabstudio.veganapp.R;
import be.tabtabstudio.veganapp.data.entities.Location;
import be.tabtabstudio.veganapp.data.entities.User;

public class SplashActivity extends AppCompatActivity {

    public static final int USER_LOGIN_CODE = 1719;
    public static final int LOCATION_PERMISSON_CODE = 1917;

    private SplashViewModel mViewModel;
    private TextView loadingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loadingTextView = findViewById(R.id.loading_text_view);

        mViewModel = ViewModelProviders.of(this).get(SplashViewModel.class);

        mViewModel.getLocationObservable().observe(this, new Observer<Location>() {
            @Override
            public void onChanged(@Nullable Location location) {
                mViewModel.handleLocationLoad(SplashActivity.this, location);
            }
        });

        mViewModel.startLoading(this);
    }

    public void showLoadingLocation() {
        loadingTextView.setText("Finding your location");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == USER_LOGIN_CODE && resultCode == RESULT_OK) {
            mViewModel.handleUserLoad(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSON_CODE: {
                if (grantResults.length > 0) {
                    mViewModel.handleLocationPermissionResult(this, grantResults[0]);
                }
                return;
            }
        }
    }

}
