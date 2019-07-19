package com.dickyrey.konsulyuk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.dickyrey.konsulyuk.Adapter.GroupMessageAdapter;
import com.dickyrey.konsulyuk.Adapter.MessageAdapter;
import com.dickyrey.konsulyuk.Model.AllMethods;
import com.dickyrey.konsulyuk.Model.Contacts;
import com.dickyrey.konsulyuk.Model.GroupMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class GroubChatActivity extends AppCompatActivity implements View.OnClickListener{


    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference messageDb;
    GroupMessageAdapter groupMessageAdapter;
    Contacts contacts;
    List<GroupMessage> groupMessages;

    RecyclerView rvMessage;
    EditText etMessage;
    ImageButton imgButton;

    private FirebaseAuth mAuth;

    private String currentGroupName;
    private Toolbar GroupToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groub_chat);

        GroupToolbar = findViewById(R.id.group_chat_bar_layout);
        setSupportActionBar(GroupToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Konsultasi Bersama");

        currentGroupName = getIntent().getExtras().get("groupName").toString();

        init();

    }

    private void init() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        contacts = new Contacts();

        rvMessage = findViewById(R.id.rvMessages);
        etMessage = findViewById(R.id.etMessage);
        imgButton = findViewById(R.id.btnSendGroup);
        imgButton.setOnClickListener(this);
        groupMessages = new ArrayList<>();

    }


    @Override
    public void onClick(View view) {
        if (!TextUtils.isEmpty(etMessage.getText().toString())){
            GroupMessage groupMessage = new GroupMessage(etMessage.getText().toString(), contacts.getName());
            etMessage.setText("");
            messageDb.push().setValue(groupMessage);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser currentUser = auth.getCurrentUser();

        contacts.setUid(currentUser.getUid());
        contacts.setName(currentUser.getEmail());

        database.getReference("Psikolog").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contacts = dataSnapshot.getValue(Contacts.class);
                contacts.setUid(currentUser.getUid());
                AllMethods.name = contacts.getName();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        messageDb = database.getReference().child("Groups").child(currentGroupName); //get data

        messageDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GroupMessage groupMessage = dataSnapshot.getValue(GroupMessage.class);
                groupMessage.setKey(dataSnapshot.getKey());
                groupMessages.add(groupMessage);
                displayMessages(groupMessages);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GroupMessage groupMessage = dataSnapshot.getValue(GroupMessage.class);
                groupMessage.setKey(dataSnapshot.getKey());

                List<GroupMessage> newMessages = new ArrayList<>();

                for (GroupMessage m: groupMessages){
                    if (m.getKey().equals(groupMessage.getKey())){
                        newMessages.add(groupMessage);
                    }else{
                        newMessages.add(m);
                    }
                }
                groupMessages = newMessages;
                displayMessages(groupMessages);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                GroupMessage groupMessage = dataSnapshot.getValue(GroupMessage.class);

                List<GroupMessage> newMessages = new ArrayList<GroupMessage>();
                for (GroupMessage m:groupMessages){
                    if (!m.getKey().equals(groupMessage.getKey())){
                        newMessages.add(m);
                    }
                }

                groupMessages = newMessages;
                displayMessages(groupMessages);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        groupMessages = new ArrayList<>();
    }

    private void displayMessages(List<GroupMessage> groupMessages) {
        rvMessage.setLayoutManager(new LinearLayoutManager(GroubChatActivity.this));
        groupMessageAdapter = new GroupMessageAdapter(GroubChatActivity.this, groupMessages, messageDb);
        rvMessage.setAdapter(groupMessageAdapter);
    }
}