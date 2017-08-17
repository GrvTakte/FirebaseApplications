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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by gaurav on 11/08/17.
 */

public class SignupActivity extends AppCompatActivity {

    private Button btn_signup, btn_cancel, btn_image;
    private EditText inputName, inputEmail, inputPassword, inputRepassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        this.getSupportActionBar().setTitle("FOOD-BOOK");
        this.getSupportActionBar().setElevation(0);
        this.getSupportActionBar().setSubtitle("Sign Up");
        //Buttons
        btn_signup = (Button) findViewById(R.id.button_signup);
        btn_image = (Button) findViewById(R.id.image_button);
        btn_cancel = (Button) findViewById(R.id.button_cancel);
        //EditText
        inputName = (EditText) findViewById(R.id.user_name);
        inputEmail = (EditText) findViewById(R.id.user_email);
        inputPassword = (EditText) findViewById(R.id.pass);
        inputRepassword = (EditText) findViewById(R.id.repassword);
        //ProgressBar
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //FireBaseAuth
        auth = FirebaseAuth.getInstance();

        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String name = inputName.getText().toString().trim();
               String email = inputEmail.getText().toString().trim();
               String password = inputPassword.getText().toString().trim();
               String repassword = inputRepassword.getText().toString().trim();

                if (TextUtils.isEmpty(name)){
                    inputName.setError("Enter user name");
                }else if (TextUtils.isEmpty(email)){
                    inputEmail.setError("Enter user email");
                }else if (TextUtils.isEmpty(password)){
                    inputPassword.setError("Enter user password");
                }else if (TextUtils.isEmpty(repassword)){
                    inputRepassword.setError("Please confirm password");
                }else if (password == repassword){
                    inputPassword.setError("Please enter password again ");
                    inputRepassword.setError("Password doesn't match to above password");
                }
                progressBar.setVisibility(View.VISIBLE);
                    auth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);

                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(SignupActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
            }
        });
    }
}
