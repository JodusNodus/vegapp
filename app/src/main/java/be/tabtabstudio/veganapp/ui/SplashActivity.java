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

    private static final int USER_LOGIN_CODE = 1719;
    private static final int LOCATION_PERMISSON_CODE = 1917;

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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPersmission();
        } else {
            startLocationListener();
        }
    }

    private void requestLocationPersmission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_CONTACTS)) {

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSON_CODE);
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationListener() {
        loadingTextView.setText("Finding your location");
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(android.location.Location location) {
                locationManager.removeUpdates(this);

                double lat = location.getLatitude();
                double lng = location.getLongitude();
                mViewModel.setLocation(lat, lng);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSON_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationListener();
                } else {
                    Toast.makeText(this, "Pls dont deny", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

}
