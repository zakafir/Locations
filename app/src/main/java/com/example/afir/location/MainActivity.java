package com.example.afir.location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final String LOG_TAG = "TestAppLocation";
    private TextView latitudeLocationTxt,longitudeLocationTxt;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latitudeLocationTxt = (TextView) findViewById(R.id.latitudeLocationTxt);
        longitudeLocationTxt = (TextView) findViewById(R.id.longitudeLocationTxt);
        buildGoogleApiClient();
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Here we connect the Google API client to the services
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Here we disconnect the Google API client to the services
        if(mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        int permissionCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        /*mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //to update the location every second
        mLocationRequest.setInterval(1000);*/


        if(permissionCheck == PackageManager.PERMISSION_GRANTED){
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(mLastLocation != null) {
                latitudeLocationTxt.setText(String.valueOf(mLastLocation.getLatitude()));
                longitudeLocationTxt.setText(String.valueOf(mLastLocation.getLongitude()));
            }

        }

            //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(LOG_TAG,"GoogleApiClient connection has been suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(LOG_TAG,"GoogleApiClient connection has been failed: "+connectionResult.getErrorCode());
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d(LOG_TAG,location.toString());
        latitudeLocationTxt.setText(String.valueOf(location.getLatitude()));
        longitudeLocationTxt.setText(String.valueOf(location.getLongitude()));
    }
}
