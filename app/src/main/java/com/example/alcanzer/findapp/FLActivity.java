package com.example.alcanzer.findapp;

import android.content.Intent;
import android.os.Debug;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FLActivity extends AppCompatActivity {
    ListView userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        userList = (ListView) findViewById(R.id.uList);
        final ArrayList<String> uList= new ArrayList<>();
        //Query to find all the users except the current user.
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        userQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(final List<ParseUser> objects, ParseException e) {
                for(ParseUser object:objects){
                    uList.add(object.getUsername());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uList);
                userList.setAdapter(arrayAdapter);
                userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Launch MapActivity for the selected user.
                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        intent.putExtra("UserObj", objects.get(position).getParseGeoPoint("Location"));
                        intent.putExtra("Username", objects.get(position).getUsername());
                        startActivity(intent);
                    }
                });
            }
        });
    }
}
