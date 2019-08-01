package com.dickyrey.konsulyuk.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.dickyrey.konsulyuk.Model.Artikel;
import com.dickyrey.konsulyuk.NewPostActivity;
import com.dickyrey.konsulyuk.R;
import com.dickyrey.konsulyuk.ViewHolder.ArtikelList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArtikelFragment extends Fragment {

    private View ArtikelFragmentView;
    private Button fabAdd;

    private DatabaseReference databaseArtikels;
    private ListView listViewArtikel;
    private RecyclerView recycler_artikel;
    private List<Artikel> artikelList = new ArrayList<>();

    public ArtikelFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ArtikelFragmentView =  inflater.inflate(R.layout.fragment_artikel, container, false);


        databaseArtikels = FirebaseDatabase.getInstance().getReference("Artikel");
        recycler_artikel = ArtikelFragmentView.findViewById(R.id.reycler_artikel);

//        fabAdd = ArtikelFragmentView.findViewById(R.id.fab_add_group);
//        fabAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getContext(), NewPostActivity.class));
//            }
//        });

        load();


        return ArtikelFragmentView;
    }

    private void load(){
        databaseArtikels.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Artikel artikel = snapshot.getValue(Artikel.class);
                    Artikel data = new Artikel();
                    String judul = artikel.getJudul();
                    String desc = artikel.getDesc();
                    String gambar = artikel.getImage_thumb();

                    data.setJudul(judul);
                    data.setDesc(desc);
                    data.setImage_thumb(gambar);
                    artikelList.add(data);
                }
                ArtikelList recycler = new ArtikelList(artikelList);
                RecyclerView.LayoutManager layoutmanager = new LinearLayoutManager(getActivity());
                recycler_artikel.setLayoutManager(layoutmanager);
                recycler_artikel.setItemAnimator( new DefaultItemAnimator());
                recycler_artikel.setAdapter(recycler);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}

