package be.tabtabstudio.veganapp.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import be.tabtabstudio.veganapp.data.VegRepository;
import be.tabtabstudio.veganapp.data.entities.Location;
import be.tabtabstudio.veganapp.data.entities.User;

public class SplashViewModel extends ViewModel {
    public LiveData<Location> getLocationObservable() { return VegRepository.getInstance().getLocation(); }
    public LiveData<User> getUserObservable() { return VegRepository.getInstance().getUser(); }

    public void fetchProduct(long ean) {
        VegRepository.getInstance().fetchProduct(ean);
    }

    public void setLocation(double lat, double lng) {
        VegRepository.getInstance().setLocation(new Location(lat, lng));
    }

    public void startLoading(SplashActivity context) {
        loadUser(context);
    }

    private void loadUser(SplashActivity context) {
        User user = getUserObservable().getValue();

        if (user == null) {
            Intent k = new Intent(context, LoginActivity.class);
            context.startActivityForResult(k, SplashActivity.USER_LOGIN_CODE);
        }
    }

    public void handleUserLoad(SplashActivity context) {
        User user = getUserObservable().getValue();
        loadLocation(context);
    }

    private void loadLocation(SplashActivity context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPersmission(context);
        } else {
            startLocationListener(context);
        }
    }

    private void requestLocationPersmission(SplashActivity context) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                Manifest.permission.READ_CONTACTS)) {

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

        } else {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    SplashActivity.LOCATION_PERMISSON_CODE);
        }
    }

    public void handleLocationPermissionResult(SplashActivity context, int result) {
        // If request is cancelled, the result arrays are empty.
        if (result == PackageManager.PERMISSION_GRANTED) {
            startLocationListener(context);
        } else {
            Toast.makeText(context, "Pls dont deny", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationListener(SplashActivity context) {
        context.showLoadingLocation();
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

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

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    public void handleLocationLoad(SplashActivity context, Location location) {
        if (location == null) {
            Toast.makeText(context, "Location set Failed", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(context, "Location set Succesfull", Toast.LENGTH_SHORT).show();
        loadProductDetails(context);
    }

    private void loadProductDetails(SplashActivity context) {
        Intent k = new Intent(context, ProductDetailsActivity.class);
        context.startActivity(k);
        fetchProduct(555555555555L);
    }

}
