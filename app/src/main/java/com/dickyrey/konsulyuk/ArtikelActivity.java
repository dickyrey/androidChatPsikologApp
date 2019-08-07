package com.dickyrey.konsulyuk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.dickyrey.konsulyuk.Model.Artikel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ArtikelActivity extends AppCompatActivity {

    private TextView artikel_judul, artikel_desc, artikel_topik;
    private ImageView artikel_gambar;
    private Toolbar artikel_toolbar;

    private String artikelID, artikelTopik, artikelJudul, artikelDesc, artikelImage;

    private DatabaseReference artikelRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artikel);

        artikel_topik = findViewById(R.id.artikel_topik);
        artikel_judul = findViewById(R.id.artikel_judul);
        artikel_desc = findViewById(R.id.artikel_deskripsi);
        artikel_toolbar = findViewById(R.id.artikel_toolbar);
        artikel_gambar = findViewById(R.id.artikel_image);


        setSupportActionBar(artikel_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        artikel_toolbar.setTitle(" ");

        artikelID = getIntent().getExtras().get("artikel_id").toString();
        artikelTopik = getIntent().getExtras().get("topik").toString();
        artikelJudul = getIntent().getExtras().get("judul").toString();
        artikelDesc = getIntent().getExtras().get("desc").toString();
        artikelImage = getIntent().getExtras().get("image_thumb").toString();


        artikelRef = FirebaseDatabase.getInstance().getReference("Artikel").child(artikelID);

        loadArtikel();

    }

    private void loadArtikel() {
        artikelRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("image_thumb"))){
                    String topik = dataSnapshot.child("topik").getValue().toString();
                    String judul = dataSnapshot.child("judul").getValue().toString();
                    String desc = dataSnapshot.child("desc").getValue().toString();
                String gambar = dataSnapshot.child("image_thumb").getValue().toString();

                    artikel_topik.setText(topik);
                    artikel_judul.setText(judul);
                    artikel_desc.setText(desc);
                    Picasso.get().load(gambar).into(artikel_gambar);

                }else {
                    String gambar = dataSnapshot.child("image_thumb").getValue().toString();
                    String topik = dataSnapshot.child("topik").getValue().toString();
                    String judul = dataSnapshot.child("judul").getValue().toString();
                    String desc = dataSnapshot.child("desc").getValue().toString();

                    artikel_topik.setText(topik);
                    artikel_judul.setText(judul);
                    artikel_desc.setText(desc);

                    Picasso.get().load(gambar).into(artikel_gambar);
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
