package com.gaurav.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getName();
    private TextView txtDetails;
    private EditText inputName, inputEmail;
    private Button btnSave;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Displaying toolbar icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        txtDetails = (TextView) findViewById(R.id.txt_user);
        inputName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        btnSave = (Button) findViewById(R.id.btn_save);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("posts");

        //get reference to user node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        //store app_title value to app_title node
        mFirebaseInstance.getReference("app_title").setValue("Realtime database");

        //app_title change listener
        mFirebaseInstance.getReference("app_title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String appTitle = dataSnapshot.getValue(String.class);
                getSupportActionBar().setTitle(appTitle);

                android.support.v4.app.NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                        .setContentTitle("App id changed")
                        .setContentText(appTitle)
                        .setSmallIcon(R.mipmap.ic_launcher);
                int mNotificationId = 001;
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.notify(mNotificationId,mBuilder.build());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to read app title value.", databaseError.toException());
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputName.getText().toString();
                String email = inputEmail.getText().toString();

                if (TextUtils.isEmpty(userId)) {
                    createUser(name, email);
                } else {
                    updateUser(name, email);
                }
            }
        });
        toggleButton();
    }

    private void toggleButton() {
        if (TextUtils.isEmpty(userId)) {
            btnSave.setText("Save");
        } else {
            btnSave.setText("Update");
        }
    }

    private void createUser(String name, String email) {
        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }

        User user = new User(name, email);

        mFirebaseDatabase.child(userId).setValue(user);

        addUserChangeListener();
    }

    private void addUserChangeListener() {
        // User data change listener
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                // Check for null
                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }

                Log.e(TAG, "User data is changed!" + user.name + ", " + user.email);

                // Display newly updated name and email
                txtDetails.setText(user.name + ", " + user.email);

                // clear edit text
                inputEmail.setText("");
                inputName.setText("");

                toggleButton();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to read user", databaseError.toException());
            }
        });
    }

    private void updateUser(String name, String email){
        // updating the user via child nodes
        if (!TextUtils.isEmpty(name))
            mFirebaseDatabase.child(userId).child("name").setValue(name);

        if (!TextUtils.isEmpty(email))
            mFirebaseDatabase.child(userId).child("email").setValue(email);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            toggleButton();
        }

        return super.onOptionsItemSelected(item);
    }
}
