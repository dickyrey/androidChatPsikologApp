package com.dickyrey.konsulyuk.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dickyrey.konsulyuk.R;
import com.dickyrey.konsulyuk.SettingsActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    View AkunFragmentView;
    private CircleImageView akun_image;
    private TextView akun_name, akun_pendidikan, akun_email, akun_tempat_praktek, akun_web;
    private FloatingActionButton button_setting;
    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private StorageReference UserProfileImageRef;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AkunFragmentView = inflater.inflate(R.layout.fragment_account, container, false);

        akun_name = AkunFragmentView.findViewById(R.id.akun_name);
        akun_pendidikan = AkunFragmentView.findViewById(R.id.akun_pendidikan);
        akun_email = AkunFragmentView.findViewById(R.id.akun_email);
        akun_tempat_praktek = AkunFragmentView.findViewById(R.id.akun_tempat_praktek);
        akun_web = AkunFragmentView.findViewById(R.id.akun_web);
        akun_image = AkunFragmentView.findViewById(R.id.akun_image);
        button_setting = AkunFragmentView.findViewById(R.id.button_Setting);

        mAuth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        loadAkun();

        button_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        return AkunFragmentView;
    }

    private void loadAkun() {
        RootRef.child("Psikolog").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name") && (dataSnapshot.hasChild("image")))){
                            String retrieveName = dataSnapshot.child("name").getValue().toString();
                            String retrievePendidikan = dataSnapshot.child("pendidikan").getValue().toString();
                            String retrieveEmail = dataSnapshot.child("email").getValue().toString();
                            String retrieveTempatPraktek = dataSnapshot.child("tempatpraktek").getValue().toString();
                            String retrieveWeb = dataSnapshot.child("web").getValue().toString();
                            String retrieveImage = dataSnapshot.child("image").getValue().toString();

                            akun_name.setText(retrieveName);
                            akun_pendidikan.setText(retrievePendidikan);
                            akun_email.setText(retrieveEmail);
                            akun_tempat_praktek.setText(retrieveTempatPraktek);
                            akun_web.setText(retrieveWeb);
                            Picasso.get().load(retrieveImage).into(akun_image);

                        }else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name"))){
                            String retrieveName = dataSnapshot.child("name").getValue().toString();
                            String retrievePendidikan = dataSnapshot.child("pendidikan").getValue().toString();
                            String retrieveEmail = dataSnapshot.child("email").getValue().toString();
                            String retrieveTempatPraktek = dataSnapshot.child("tempatpraktek").getValue().toString();
                            String retrieveWeb = dataSnapshot.child("web").getValue().toString();

                            akun_name.setText(retrieveName);
                            akun_pendidikan.setText(retrievePendidikan);
                            akun_email.setText(retrieveEmail);
                            akun_tempat_praktek.setText(retrieveTempatPraktek);
                            akun_web.setText(retrieveWeb);

                        }else {
                            akun_name.setVisibility(View.VISIBLE);
                            Toast.makeText(getContext(), "Perbarui profile dengan benar", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


}
