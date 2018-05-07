package be.tabtabstudio.veganapp.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import be.tabtabstudio.veganapp.data.VegRepository;
import be.tabtabstudio.veganapp.data.entities.Location;
import be.tabtabstudio.veganapp.data.entities.User;
import be.tabtabstudio.veganapp.data.network.requestBodies.UserLoginBody;

public class SplashViewModel extends ViewModel {

    private VegRepository repo;

    @Override
    public void setContext(Context context) {
        super.setContext(context);
        this.repo = VegRepository.getInstance(context);
    }

    public LiveData<Location> getLocationObservable() { return repo.getLocationObservable(); }
    public LiveData<User> getUserObservable() { return repo.getUserObservable(); }

    public void setLocation(double lat, double lng) {
        repo.setLocation(new Location(lat, lng));
    }

    public void startLoading() {
        loadUser();
    }

    private void loadUser() {
        UserLoginBody userLoginBody = repo.getPersistedLogin();

        if (userLoginBody != null) {
            repo.login(userLoginBody);
        } else {
            openSignUpActivity();
        }
    }

    private void openSignUpActivity() {
        Intent k = new Intent(getContext(), SignupActivity.class);
        ((SplashActivity) getContext()).finish();
        ((SplashActivity) getContext()).startActivityForResult(k, SplashActivity.USER_LOGIN_CODE);
    }

    public void handleUserSuccess() {
        User user = getUserObservable().getValue();
        loadLocation();
    }

    public void handleUserFailed() {
       openSignUpActivity();
    }

    private void loadLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPersmission();
        } else {
            startLocationListener();
        }
    }

    private void requestLocationPersmission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getContext(),
                Manifest.permission.READ_CONTACTS)) {

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

        } else {
            ActivityCompat.requestPermissions((Activity) getContext(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    SplashActivity.LOCATION_PERMISSON_CODE);
        }
    }

    public void handleLocationPermissionResult(int result) {
        // If request is cancelled, the result arrays are empty.
        if (result == PackageManager.PERMISSION_GRANTED) {
            startLocationListener();
        } else {
            Toast.makeText(getContext(), "Pls dont deny", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationListener() {
        ((SplashActivity) getContext()).showLoadingLocation();
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(android.location.Location location) {
                locationManager.removeUpdates(this);

                double lat = location.getLatitude();
                double lng = location.getLongitude();
                setLocation(lat, lng);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    public void handleLocationLoad(Location location) {
        if (location == null) {
            Log.i("location", "loc set failed");
            return;
        }
        Log.i("location", "loc set success");
        loadProductDetails();
    }

    private void loadProductDetails() {
        Intent k = new Intent(getContext(), MainActivity.class);
        k.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(k);
    }

}
