package com.dickyrey.konsulyuk.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dickyrey.konsulyuk.FindFriendsActivity;
import com.dickyrey.konsulyuk.GroubChatActivity;
import com.dickyrey.konsulyuk.KlienActivity;
import com.dickyrey.konsulyuk.MainActivity;
import com.dickyrey.konsulyuk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
                RequestNewGroup();
            }
        });


        return MenuFragmentView;
    }


    private void RequestNewGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuFragmentView.getContext(), R.style.AlertDialog);
        builder.setTitle("Masukkan nama Grup");

        final EditText groupNameField = new EditText(MenuFragmentView.getContext());
        groupNameField.setHint("Apa yang kamu ingin diskusikan?");
        builder.setView(groupNameField);

        builder.setPositiveButton("Buat", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String groupName = groupNameField.getText().toString();

                if (TextUtils.isEmpty(groupName)){
                    Toast.makeText(MenuFragmentView.getContext(), "Tolong tulis nama grup!", Toast.LENGTH_SHORT).show();
                }else{
                    CreateNewGroup(groupName);
                }
            }
        });
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    private void CreateNewGroup(final String groupName) {
        RootRef.child("Groups").child(groupName).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MenuFragmentView.getContext(), groupName + " telah berhasil dibuat", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
