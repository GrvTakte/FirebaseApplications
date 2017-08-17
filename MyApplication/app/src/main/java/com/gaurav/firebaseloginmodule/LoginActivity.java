package com.gaurav.firebaseloginmodule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by gaurav on 11/08/17.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button btn_login,btn_signup,btn_reset;
    FirebaseAuth auth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check user already logged in or not
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser()!= null){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }

        setContentView(R.layout.login_activity);
        this.getSupportActionBar().setTitle("FOOD-BOOK");
        this.getSupportActionBar().setElevation(0);
        this.getSupportActionBar().setSubtitle("Login");

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btn_login = (Button) findViewById(R.id.login);
        btn_reset = (Button) findViewById(R.id.btn_resetpass);
        btn_signup = (Button) findViewById(R.id.btn_signup);

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "Enter the email address", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Enter the password", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("User_id",auth.getCurrentUser().getUid());
                                        intent.putExtra("User_displayName",auth.getCurrentUser().getDisplayName());
                                        intent.putExtra("User_Email",auth.getCurrentUser().getEmail());
                                        intent.putExtra("User_IdToken",auth.getCurrentUser().getIdToken(true).toString());
                                        intent.putExtra("User_phoneNumber",auth.getCurrentUser().getPhoneNumber());
                                        intent.putExtra("User_PhotoUrl",auth.getCurrentUser().getPhotoUrl());
                                        intent.putExtra("User_ProviderData",auth.getCurrentUser().getProviderData().toString());
                                        intent.putExtra("User_ProvidersId",auth.getCurrentUser().getProviderId());
                                        intent.putExtra("User_Providers",auth.getCurrentUser().getProviders().toString());
                                        intent.putExtra("User_Token",auth.getCurrentUser().getToken(true).toString());
                                        startActivity(intent);
                                    } else {
                                        inputEmail.setError("Something went wrong with email");
                                        inputPassword.setError("Something went wrong with password");
                                    }
                                }
                            });
                }
            }
        });


    }
}
