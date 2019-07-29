package com.dickyrey.konsulyuk.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dickyrey.konsulyuk.Model.AllMethods;
import com.dickyrey.konsulyuk.Model.GroupMessage;
import com.dickyrey.konsulyuk.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class GroupMessageAdapter extends RecyclerView.Adapter<GroupMessageAdapter.GroupMessageViewHolder> {

    Context context;
    List<GroupMessage> groupMessages;
    DatabaseReference messageDB;

    public GroupMessageAdapter(Context context, List<GroupMessage> groupMessages, DatabaseReference messageDB) {
        this.context = context;
        this.groupMessages = groupMessages;
        this.messageDB = messageDB;
    }

    @NonNull
    @Override
    public GroupMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_group_message,parent,false);
        return new GroupMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupMessageViewHolder holder, int position) {
        GroupMessage groupMessage = groupMessages.get(position);
        if (groupMessage.getName().equals(AllMethods.name)){
            //getimagelater
            holder.tvTitle.setText("You \n"+ groupMessage.getMessage());
            holder.tvTitle.setGravity(Gravity.START);
            holder.linear1.setBackgroundColor(Color.parseColor("#EF9E73"));

        }
        else{
            //getimagelater
            holder.tvTitle.setText(groupMessage.getName() + " \n" +groupMessage.getMessage());
            holder.tvTitle.setBackgroundColor(Color.parseColor("#43bd42"));
            holder.ibDelete.setVisibility(View.GONE);

        }
    }

    @Override
    public int getItemCount() {
        return groupMessages.size();
    }

    public class GroupMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageButton ibDelete;
        LinearLayout linear1;


        public GroupMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ibDelete = itemView.findViewById(R.id.ibDelete);
            linear1 = itemView.findViewById(R.id.l1Message);


            ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    messageDB.child(groupMessages.get(getAdapterPosition()).getKey()).removeValue();
                }
            });
        }
    }
}
