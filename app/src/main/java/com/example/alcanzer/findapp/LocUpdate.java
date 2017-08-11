package com.example.alcanzer.findapp;

import android.Manifest;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class LocUpdate extends Service {
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private SharedPreferences mSharedPref;
    ParseQuery<ParseUser> userQuery;
    ParseUser user;
    ParseGeoPoint point;

    public LocUpdate() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Initialize Parse server connection
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("6571850eefaf7ac175b0b818d76b29bb04fdc900")
                .server("http://ec2-54-200-254-174.us-west-2.compute.amazonaws.com:80/parse")
                .build()
        );
        //Get user object ID from SharedPrefs
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String UID = mSharedPref.getString("ObjId", "");
        userQuery = ParseUser.getQuery();
        userQuery.whereEqualTo("objectId", UID);
        try {
            //Get the current user object
            user = userQuery.find().get(0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (user != null) {
            //Start tracking once the user object is assigned
            Toast.makeText(getApplicationContext(), "Tracking started", Toast.LENGTH_SHORT).show();
            mLocationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    updateDB(location);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, mLocationListener);
        }
        else{
            stopSelf();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Unregister on stopping the service
        mLocationManager.removeUpdates(mLocationListener);
        Toast.makeText(getApplicationContext(), "Tracking stopped", Toast.LENGTH_SHORT).show();
    }

    private void updateDB(Location location){
        //Create a Parse GeoPoint to store the LatLng in the Parse Server.
        point = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
        user.put("Location", point);
        user.saveInBackground();
    }
}
