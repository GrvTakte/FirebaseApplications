package com.gaurav.artists;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 101;
    private EditText artistsAdd;
    private Spinner spinnerAdd;
    private Button buttonAdd, upload_image;
    private TextView textFilePath;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ListView listViewArtists;
    private ArrayList<Artists> arrayList;
    private StorageReference mStorageRef;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("artists");

        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference();

        artistsAdd = (EditText) findViewById(R.id.artistsAdd);
        spinnerAdd = (Spinner) findViewById(R.id.spinnerAdd);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        upload_image = (Button) findViewById(R.id.upload_image);
        listViewArtists = (ListView) findViewById(R.id.listviewArtists);
        textFilePath = (TextView) findViewById(R.id.file_path);
        arrayList = new ArrayList<>();

        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addArtists();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot artistsSnapShot :dataSnapshot.getChildren()){
                    Artists artist = artistsSnapShot.getValue(Artists.class);
                    arrayList.add(artist);
                }
                ArtistAdapter adapter = new ArtistAdapter(MainActivity.this,R.layout.list_item,arrayList);
                listViewArtists.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == PICK_IMAGE_REQUEST) && (resultCode == RESULT_OK)){
            final Uri filePath = data.getData();
            try{
                final StorageReference childRef = mStorageRef.child(data.getDataString());
                UploadTask uploadTask = childRef.putFile(filePath);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(MainActivity.this, "Image uploaded successfully", Toast.LENGTH_LONG).show();
                        childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                textFilePath.setText(uri.toString());
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Image failed to upload", Toast.LENGTH_LONG).show();
                    }
                });
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private void addArtists() {
        String name = artistsAdd.getText().toString().trim();
        String genres = spinnerAdd.getSelectedItem().toString();
        String imageUrl = textFilePath.getText().toString();

        if (!TextUtils.isEmpty(name)) {
            String id = myRef.push().getKey();
            Artists artists = new Artists(id, name, genres, imageUrl);
            myRef.child(id).setValue(artists);
            Toast.makeText(this, "Artist added", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(this, "Failed to add artist", Toast.LENGTH_SHORT).show();
        }
    }
}