package com.example.alcanzer.findapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by Alcanzer on 8/9/2017.
 */

public class signUpClass extends Fragment {
    Button signUp;
    EditText usernameET, passwordET, emailET;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.signup, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        signUp = (Button) getView().findViewById(R.id.signUp);
        usernameET = (EditText) getView().findViewById(R.id.usernameETSignUp);
        passwordET = (EditText) getView().findViewById(R.id.passwordETSignUp);
        emailET = (EditText) getView().findViewById(R.id.emailSignUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = new ParseUser();
                user.setUsername(usernameET.getText().toString().trim());
                user.setPassword(passwordET.getText().toString().trim());
                user.setEmail(emailET.getText().toString().trim());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e==null){
                            Toast.makeText(getContext(), "You can login now!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
