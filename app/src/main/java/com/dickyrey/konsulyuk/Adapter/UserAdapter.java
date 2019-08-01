package com.dickyrey.konsulyuk.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dickyrey.konsulyuk.Model.User;
import com.dickyrey.konsulyuk.ProfileActivity;
import com.dickyrey.konsulyuk.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;


    public UserAdapter(Context mContext, List<User> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);

        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {



        final User u = mUsers.get(position);
        holder.username.setText(u.getName());
        holder.userpendidikan.setText(u.getPendidikan());

        Picasso.get().load(u.getImage()).placeholder(R.drawable.icon_male).into(holder.profile_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent profileIntent = new Intent(mContext, ProfileActivity.class);
                profileIntent.putExtra("uid", u.getUid());
                profileIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(profileIntent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username, userpendidikan;
        public ImageView profile_image;

        public ViewHolder(View itemView){
            super(itemView);


            username = itemView.findViewById(R.id.username);
            userpendidikan = itemView.findViewById(R.id.userstatus);
            profile_image = itemView.findViewById(R.id.profile_image);


        }
    }

}
