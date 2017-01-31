package com.example.rtijerina.gps_sql_app;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements LocationListener {

    private Button gps_button;
    private TextView db_received;
    private LocationManager locationManager;
    private Location location;
    private long MIN_TIME_BW_UPDATES = 1000, MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private double latitude = 0, longitude = 0;


    private boolean isGPSEnabled, isNetworkEnabled;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the views in the activity
        gps_button = (Button)findViewById(R.id.gps_button);
        db_received = (TextView)findViewById(R.id.db_received);

        // Check to see if we have permission to change the GPS location settings
        checkLocationPermission();

        // Ask the User directly if they would like to the option to detect their location
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // It's updated every second with the best accuracy, 0 meters distance change
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
    }

    public void getLocation(View view) {
        try {
            // Boolean to check for already found gps coordinates
            boolean gps_found = false;

            // Create a new MyBackgroundTask
            MyBackgroundTask myBackgroundTask = new MyBackgroundTask(this);

            // Checks the permissions for using the LocationManager
            checkLocationPermission();

            // Create instance of the Location Manager
            locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

            // Getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // Getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {

                // First get location from Network Provider
                if ( isNetworkEnabled ) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Log.i("","Lat: " + latitude + "; " + "Lng: " + longitude);
                            gps_found = true;
                        }
                    }
                }

                // if GPS Enabled get lat/long using GPS Services
                if ( isGPSEnabled & !gps_found ) {
                   // if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                Log.i("","Lat: " + latitude + "; " + "Lng: " + longitude);
                            }
                        }
                //    }
                }

                myBackgroundTask.execute("register",latitude  + "",longitude + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getDBTable(View view){
        MyReceiveTask myReceiveTask = new MyReceiveTask(this, db_received);
        myReceiveTask.execute();
    }

    public void sendGPSLocation(View view) {
        MyBackgroundTask myBackgroundTask = new MyBackgroundTask(this);
        checkLocationPermission();
        //location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        /*locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);*/
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        myBackgroundTask.execute("register",location.getLatitude() + "",location.getLongitude() + "");
        Toast.makeText(this, "Location is : " + location.getLatitude(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String s) {}

    @Override
    public void onLocationChanged(Location location) {

        // When the location has changed then update it in the database
        //getLocation();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderDisabled(String s) {}

    public boolean checkLocationPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
