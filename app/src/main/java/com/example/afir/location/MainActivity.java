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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        latitudeLocationTxt = (TextView) findViewById(R.id.latitudeLocationTxt);
        longitudeLocationTxt = (TextView) findViewById(R.id.longitudeLocationTxt);
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
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //to update the location every second
        mLocationRequest.setInterval(1000);

        int permissionCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            Log.d(LOG_TAG,"Permission for location is OK");
        }
        else{
            Log.d(LOG_TAG,"Permission for location is NG");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(LOG_TAG,"GoogleApiClient connection has been suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(LOG_TAG,"GoogleApiClient connection has been failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(LOG_TAG,location.toString());
        latitudeLocationTxt.setText(String.valueOf(location.getLatitude()));
        longitudeLocationTxt.setText(String.valueOf(location.getLongitude()));
    }
}
