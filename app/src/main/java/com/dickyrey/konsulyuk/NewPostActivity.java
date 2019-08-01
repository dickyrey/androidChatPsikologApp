package com.dickyrey.konsulyuk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;

import com.bumptech.glide.load.data.ByteArrayFetcher;
import com.dickyrey.konsulyuk.Fragment.ArtikelFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class NewPostActivity extends AppCompatActivity {


    private Toolbar newPostToolbar;
    private ImageView newPostImage;
    private EditText newPostDescription, newPostJudul;
    private Button newPostButton;
    private Uri newPostImageUri;

    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;


    private Bitmap compressedImageFile;
    private String currentUserID, saveCurrentTime, saveCurrentDate;;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        currentUserID = firebaseAuth.getCurrentUser().getUid();

        newPostToolbar = findViewById(R.id.newPostToolbar);
        setSupportActionBar(newPostToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Tambah Artikel Baru");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        newPostDescription = findViewById(R.id.newPostDescription);
        newPostJudul = findViewById(R.id.newPostJudul);
        newPostImage = findViewById(R.id.newPostImage);
        newPostButton = findViewById(R.id.newPostButton);
        newPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512,512)
                        .setAspectRatio(1,1)
                        .start(NewPostActivity.this);

            }
        });
        newPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String desc = newPostDescription.getText().toString();
                final String judul = newPostJudul.getText().toString();
                if (!TextUtils.isEmpty(desc) && !TextUtils.isEmpty(judul) && newPostImage != null){

                    final String randomName = UUID.randomUUID().toString();
                    StorageReference filePath = storageReference.child("artikel_images").child(randomName + ".jpg");

                    filePath.putFile(newPostImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {

                            final String downloadUri = task.getResult().getDownloadUrl().toString();

                            if (task.isSuccessful()){

                                File newImageFile = new File(newPostImageUri.getPath());

                                try {
                                    compressedImageFile = new Compressor(NewPostActivity.this)
                                            .setMaxHeight(200)
                                            .setMaxWidth(200)
                                            .setQuality(2)
                                            .compressToBitmap(newImageFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                ByteArrayOutputStream bous = new ByteArrayOutputStream();
                                compressedImageFile.compress(Bitmap.CompressFormat.JPEG,100,bous);
                                byte[] thumbData = bous.toByteArray();

                                UploadTask uploadTask = storageReference
                                        .child("artikel_images/thumbs")
                                        .child(randomName + ".jpg")
                                        .putBytes(thumbData);
                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        DatabaseReference artikelKeyRef = firebaseDatabase.getReference()
                                                .child("Artikel")
                                                .push();

                                        String downloadThumbUri = taskSnapshot.getDownloadUrl().toString();


                                        String artikelPushID = artikelKeyRef.getKey();

                                        Map<String, Object> postMap = new HashMap<>();
                                        postMap.put("artikel_id", artikelPushID);
                                        postMap.put("image_url", downloadUri);
                                        postMap.put("image_thumb", downloadThumbUri);
                                        postMap.put("desc", desc);
                                        postMap.put("judul", judul);
                                        postMap.put("time", saveCurrentTime);
                                        postMap.put("date", saveCurrentDate);
                                        postMap.put("user_id", currentUserID);


                                        Map artikelBodyDetails = new HashMap();
                                        artikelBodyDetails.put(artikelPushID, postMap);

                                        firebaseDatabase.getReference("Artikel")
                                                .updateChildren(artikelBodyDetails)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Toast.makeText(NewPostActivity.this, "Artikel Berhasil di Unggah", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(NewPostActivity.this, MainActivity.class));
                                                            finish();
                                                        }else{

                                                        }
                                                    }
                                                });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                            }else{

                            }
                        }
                    });

                }
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK){

                newPostImageUri = result.getUri();
                newPostImage.setImageURI(newPostImageUri);



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception exception = result.getError();
            }
        }
    }
}
