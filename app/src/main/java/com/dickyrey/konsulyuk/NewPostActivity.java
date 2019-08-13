package com.dickyrey.konsulyuk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
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
    private EditText newPostDescription, newPostJudul, newPostTopik;
    private Uri newPostImageUri = null;

    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference artikelRef;
    private FirebaseAuth firebaseAuth;

    private ProgressDialog loadingBar;

    private Bitmap compressedImageFile;
    private String currentUserID, saveCurrentTime, saveCurrentDate, currentArtikelID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        currentUserID = firebaseAuth.getCurrentUser().getUid();
        newPostToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(newPostToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Tambah Artikel Baru");

        artikelRef = FirebaseDatabase.getInstance().getReference("Artikel");

        loadingBar = new ProgressDialog(this);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        newPostDescription = findViewById(R.id.newPostDeskripsi);
        newPostJudul = findViewById(R.id.newPostJudul);
        newPostTopik = findViewById(R.id.newPostTopik);
        newPostImage = findViewById(R.id.newPostImage);
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

//        retrieveArtikel();

    }

    private void retrieveArtikel() {
        artikelRef.child(currentArtikelID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("image_url"))){
                            String topik = dataSnapshot.child("topik").getValue().toString();
                            String judul = dataSnapshot.child("judul").getValue().toString();
                            String desc = dataSnapshot.child("desc").getValue().toString();
                            String gambar = dataSnapshot.child("image_url").getValue().toString();

                            newPostTopik.setText(topik);
                            newPostJudul.setText(judul);
                            newPostDescription.setText(desc);
                            Picasso.get().load(gambar).into(newPostImage);

                        }else {
                            String topik = dataSnapshot.child("topik").getValue().toString();
                            String judul = dataSnapshot.child("judul").getValue().toString();
                            String desc = dataSnapshot.child("desc").getValue().toString();

                            String gambar = dataSnapshot.child("image_url").getValue().toString();

                            newPostTopik.setText(topik);
                            newPostJudul.setText(judul);
                            newPostDescription.setText(desc);
                            Picasso.get().load(gambar).into(newPostImage);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }else{

            final String topik = newPostTopik.getText().toString();
            final String judul = newPostJudul.getText().toString();
            final String desc = newPostDescription.getText().toString();
            if (TextUtils.isEmpty(topik) && newPostImageUri != null){
                Toast.makeText(this, "Isi Kolom yang belum terisi!", Toast.LENGTH_SHORT).show();

            }
            if (TextUtils.isEmpty(judul)){
                Toast.makeText(this, "Isi Kolom yang belum terisi!", Toast.LENGTH_SHORT).show();
            }
            if (TextUtils.isEmpty(desc)){
                Toast.makeText(this, "Isi Kolom yang belum terisi!", Toast.LENGTH_SHORT).show();
            }else{
                loadingBar.setTitle("Mengunggah Artikel");
                loadingBar.setMessage("Mohon tunggu....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                final String randomName = UUID.randomUUID().toString();
                StorageReference filePath = storageReference.child("artikel_images").child(randomName + ".jpg");

                filePath.putFile(newPostImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {

                        final String downloadUri = task.getResult().getDownloadUrl().toString();

                        if (task.isSuccessful()){
                            loadingBar.dismiss();
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

                                    String artikelKeyRef = firebaseDatabase.getReference("Artikel")
                                            .push()
                                            .getKey();

                                    String downloadThumbUri = taskSnapshot.getDownloadUrl().toString();


//                                        String artikelPushID = artikelKeyRef.getKey();

                                    Map<String, Object> postMap = new HashMap<>();
                                    postMap.put("artikel_id", artikelKeyRef);
                                    postMap.put("image_url", downloadUri);
                                    postMap.put("image_thumb", downloadThumbUri);
                                    postMap.put("topik", topik);
                                    postMap.put("judul", judul);
                                    postMap.put("desc", desc);
                                    postMap.put("time", saveCurrentTime);
                                    postMap.put("date", saveCurrentDate);
                                    postMap.put("user_id", currentUserID);


                                    Map artikelBodyDetails = new HashMap();
                                    artikelBodyDetails.put(artikelKeyRef, postMap);

                                    firebaseDatabase.getReference("Artikel")
                                            .updateChildren(artikelBodyDetails)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(NewPostActivity.this, "Artikel Berhasil di Unggah", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(NewPostActivity.this, MainActivity.class));
                                                        loadingBar.dismiss();
                                                        finish();
                                                    }else{

                                                    }
                                                    loadingBar.dismiss();
                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });

                        }else{
                            loadingBar.dismiss();
                        }
                    }
                });
            }





        }

        return super.onOptionsItemSelected(item);

    }
}
