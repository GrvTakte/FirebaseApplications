package com.gaurav.firebaseloginmodule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private Button btnUser, btnPosts, btnFriends;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mReference = FirebaseDatabase.getInstance().getReference("posts");

        btnUser = (Button) findViewById(R.id.button_user);
        btnFriends = (Button) findViewById(R.id.button_friendlist);
        btnPosts = (Button) findViewById(R.id.button_posts);

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,UserProfile.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        String id = mReference.push().getKey();
        mReference.child(id).child("name").setValue("Gaurav");
        mReference.child(id).child("day").setValue("Monday");
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
