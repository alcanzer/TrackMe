package com.example.alcanzer.findapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ProfileActivity extends AppCompatActivity {
    TextView mTextView;
    EditText oldPassword, newPassword1, newPassword2, email;
    Button save, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mTextView = (TextView) findViewById(R.id.usernameProfile);
        mTextView.setText(ParseUser.getCurrentUser().getUsername());
        oldPassword = (EditText) findViewById(R.id.oldPasswordProfile);
        newPassword1 = (EditText) findViewById(R.id.newPasswordProfile1);
        newPassword2 = (EditText) findViewById(R.id.newPasswordProfile2);
        email = (EditText) findViewById(R.id.emailProfile);
        email.setText(ParseUser.getCurrentUser().getEmail());
        back = (Button) findViewById(R.id.back);
        save = (Button) findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldpass, newpass1, newpass2, Xmail;
                oldpass = oldPassword.getText().toString().trim();
                newpass1 = newPassword1.getText().toString().trim();
                newpass2 = newPassword2.getText().toString().trim();
                Xmail = email.getText().toString().trim();
                validateInfo(oldpass, newpass1, newpass2, Xmail);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void validateInfo(String pass, final String pass1, String pass2, final String email) {
        //Check if both the New Password fields are the same string.
        if (pass1.equals(pass2)) {
            //Login the user using the password from the Old password field(Check that old password is correct)
            //If everything is validated, the user can change the password and the email.
            ParseUser.logInInBackground(ParseUser.getCurrentUser().getUsername(), pass, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null) {
                        if(email != null) {
                            user.setEmail(email);
                        }
                        user.setPassword(pass1);
                        user.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e == null){
                                    Toast.makeText(getApplicationContext(), "Successfully Saved.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Wrong old password", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Please enter the same password twice", Toast.LENGTH_SHORT).show();
        }
    }
}
