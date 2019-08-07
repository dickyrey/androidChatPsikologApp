package com.dickyrey.konsulyuk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private FloatingActionButton button_change_foto, button_save;
    private EditText setting_name, setting_tempat_praktek, setting_pendidikan, setting_email, setting_web;
    private Spinner setting_genre;
    private ImageView setting_image;
    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private Uri mainImageUri = null;

    private static final int GalleryPick = 1;
    private StorageReference UserProfileImageRef;
    private ProgressDialog loadingBar;
    private Toolbar SettingsToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SettingsToolbar = findViewById(R.id.setting_toolbar);
        setSupportActionBar(SettingsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Pengaturan Akun");

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
        initializeFields();

        setting_name.setVisibility(View.VISIBLE);

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateSettings();
            }
        });

        RetrieveUserInfo();

        button_change_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(SettingsActivity.this);


            }
        });
    }

    private void initializeFields() {
        button_save = findViewById(R.id.button_save);
        button_change_foto = findViewById(R.id.button_change_photo);
        setting_name = findViewById(R.id.setting_name);
        setting_pendidikan = findViewById(R.id.setting_pendidikan);
        setting_email = findViewById(R.id.setting_email);
        setting_tempat_praktek = findViewById(R.id.setting_tempat_praktek);
        setting_image = findViewById(R.id.setting_image);
        setting_web = findViewById(R.id.setting_web);
        loadingBar = new ProgressDialog(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageUri = result.getUri();
                setting_image.setImageURI(mainImageUri);
                if (resultCode == RESULT_OK) {
                    loadingBar.setTitle("Memasang Foto Profil");
                    loadingBar.setMessage("Mohon Tunggu..");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    Uri resultUri = result.getUri();
                    StorageReference filePath = UserProfileImageRef.child(currentUserID + ".jpg");
                    filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SettingsActivity.this, "Foto Profil sukses diperbarui", Toast.LENGTH_SHORT).show();

                                final String downloadUrl = task.getResult().getDownloadUrl().toString();

                                RootRef.child("Psikolog").child(currentUserID).child("image")
                                        .setValue(downloadUrl)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(SettingsActivity.this, "Gambar berhasil disimpan!", Toast.LENGTH_SHORT).show();
                                                    loadingBar.dismiss();
                                                } else {
                                                    String message = task.getException().toString();
                                                    Toast.makeText(SettingsActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                                    loadingBar.dismiss();
                                                }
                                            }
                                        });
                            } else {
                                String message = task.getException().toString();
                                Toast.makeText(SettingsActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }

            }
        }

    }

    private void UpdateSettings() {
        String setName= setting_name.getText().toString();
        String setPendidikan = setting_pendidikan.getText().toString();
        String setEmail = setting_email.getText().toString();
        String setTempatPraktek = setting_tempat_praktek.getText().toString();
        String setWeb = setting_web.getText().toString();

        if (TextUtils.isEmpty(setName) && mainImageUri != null){
            Toast.makeText(this, "Tulis Kolom Dengan Benar!", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(setPendidikan)){
            Toast.makeText(this, "Tulis Kolom Dengan Benar!", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(setEmail)){
            Toast.makeText(this, "Tulis Kolom Dengan Benar!", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(setTempatPraktek)){
            Toast.makeText(this, "Tulis Kolom Dengan Benar!", Toast.LENGTH_SHORT).show();
        }else{
            HashMap<String, Object> profileMap = new HashMap<>();
            profileMap.put("uid", currentUserID);
            profileMap.put("name", setName);
            profileMap.put("search", setName.toLowerCase());
            profileMap.put("pendidikan", setPendidikan);
            profileMap.put("email", setEmail);
            profileMap.put("tempatpraktek", setTempatPraktek);
            profileMap.put("web", setWeb);

            StorageReference filePath = UserProfileImageRef.child(currentUserID + ".jpg");
            filePath.putFile(mainImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        Uri download_uri = task.getResult().getDownloadUrl();
//                        Toast.makeText(SettingsActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            RootRef.child("Psikolog").child(currentUserID).updateChildren(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                SendUserToMainActivity();
                                Toast.makeText(SettingsActivity.this, "Data Berhasil disimpan!", Toast.LENGTH_SHORT).show();
                            }else{
                                String message = task.getException().toString();
                                Toast.makeText(SettingsActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void RetrieveUserInfo() {
        RootRef.child("Psikolog").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name") && (dataSnapshot.hasChild("image")))){
                            String retrieveName = dataSnapshot.child("name").getValue().toString();
                            String retrievePendidikan = dataSnapshot.child("pendidikan").getValue().toString();
                            String retrieveEmail = dataSnapshot.child("email").getValue().toString();
                            String retrieveTempatPraktek = dataSnapshot.child("tempatpraktek").getValue().toString();
                            String retrieveWeb = dataSnapshot.child("web").getValue().toString();
                            String retrieveImage = dataSnapshot.child("image").getValue().toString();

                            setting_name.setText(retrieveName);
                            setting_pendidikan.setText(retrievePendidikan);
                            setting_email.setText(retrieveEmail);
                            setting_tempat_praktek.setText(retrieveTempatPraktek);
                            setting_web.setText(retrieveWeb);
                            Picasso.get().load(retrieveImage).into(setting_image);

                        }else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name"))){
                            String retrieveName = dataSnapshot.child("name").getValue().toString();
                            String retrievePendidikan = dataSnapshot.child("pendidikan").getValue().toString();
                            String retrieveEmail = dataSnapshot.child("email").getValue().toString();
                            String retrieveTempatPraktek = dataSnapshot.child("tempatpraktek").getValue().toString();
                            String retrieveWeb = dataSnapshot.child("web").getValue().toString();

                            setting_name.setText(retrieveName);
                            setting_pendidikan.setText(retrievePendidikan);
                            setting_email.setText(retrieveEmail);
                            setting_tempat_praktek.setText(retrieveTempatPraktek);
                            setting_web.setText(retrieveWeb);

                        }else {
                            setting_name.setVisibility(View.VISIBLE);
//                            Toast.makeText(SettingsActivity.this, "Perbarui profile dengan benar", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(SettingsActivity.this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

}
