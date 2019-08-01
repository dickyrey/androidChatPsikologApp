package com.dickyrey.konsulyuk.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dickyrey.konsulyuk.FindFriendsActivity;
import com.dickyrey.konsulyuk.KlienActivity;
import com.dickyrey.konsulyuk.NewPostActivity;
import com.dickyrey.konsulyuk.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MenuFragment extends Fragment {

    private View MenuFragmentView;
    private LinearLayout liniear1, liniear2, linear3;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MenuFragmentView = inflater.inflate(R.layout.fragment_menu, container, false);

        liniear1 = MenuFragmentView.findViewById(R.id.linear1);
        liniear2 = MenuFragmentView.findViewById(R.id.linear2);
        linear3 = MenuFragmentView.findViewById(R.id.linear3);

        mAuth = FirebaseAuth.getInstance();

        RootRef = FirebaseDatabase.getInstance().getReference();

        liniear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chat_klien = new Intent(getContext(), KlienActivity.class);
                startActivity(chat_klien);
            }
        });
        liniear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cari_klien = new Intent(getContext(), FindFriendsActivity.class);
                startActivity(cari_klien);
            }
        });
        linear3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NewPostActivity.class);
                startActivity(intent);
            }
        });


        return MenuFragmentView;
    }


}
