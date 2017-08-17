package com.gaurav.firebaseloginmodule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by gaurav on 14/08/17.
 */

public class UserProfile extends AppCompatActivity {

    private TextView username,useremail;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        username = (TextView) findViewById(R.id.user_name);
        useremail = (TextView) findViewById(R.id.user_email);

        String name = auth.getCurrentUser().getUid();
        String email = auth.getCurrentUser().getEmail();

        if ((!TextUtils.isEmpty(name)) || (!TextUtils.isEmpty(email))){
            username.setText(name);
            useremail.setText(email);
        }else {
            username.setText("invalid user");
            useremail.setText("invalid user");
        }

    }
}
