package com.example.alcanzer.findapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.parse.ParseUser;

public class HomeActivity extends AppCompatActivity {
    SharedPreferences mSharedPref;
    SharedPreferences.Editor editor;
    Button profile, friends, search, start, stop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        profile = (Button) findViewById(R.id.profile);
        search = (Button) findViewById(R.id.search);
        start = (Button) findViewById(R.id.startTrack);
        stop = (Button) findViewById(R.id.stopTrack);
        friends = (Button) findViewById(R.id.friends);
        //Open the user's friend list(Currently shows all users, friend list yet to be implemented)
        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FLActivity.class);
                startActivity(intent);
            }
        });
        //Opens the user's profile editing page.
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });
        //FAB for Log Out.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //Button to start the background service.
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Store current user's objectId to use it in the service.
                editor = mSharedPref.edit();
                editor.putString("ObjId", ParseUser.getCurrentUser().getObjectId());
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), LocUpdate.class);
                startService(intent);
            }
        });
        //Button to stop the background service.
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LocUpdate.class);
                stopService(intent);
            }
        });
    }
}
