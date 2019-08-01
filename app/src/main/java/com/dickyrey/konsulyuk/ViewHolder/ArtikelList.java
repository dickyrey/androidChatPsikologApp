package com.dickyrey.konsulyuk.ViewHolder;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dickyrey.konsulyuk.Model.Artikel;
import com.dickyrey.konsulyuk.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ArtikelList extends RecyclerView.Adapter<ArtikelList.MyHolder>{

    List<Artikel> listArtikel;

    public ArtikelList(List<Artikel> listArtikel){
        this.listArtikel = listArtikel;
    }

    @NonNull
    @Override
    public ArtikelList.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artikel,parent,false);

        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArtikelList.MyHolder holder, int position) {
        Artikel artikel = listArtikel.get(position);
        holder.blog_judul.setText(artikel.getJudul());
        holder.blog_desc.setText(artikel.getDesc());
        Picasso.get().load(artikel.getImage_thumb()).into(holder.blog_image);
    }

    @Override
    public int getItemCount() {
        return listArtikel.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        public TextView blog_judul, blog_desc;
        public ImageView blog_image;

        public MyHolder(@NonNull View listItemView) {
            super(listItemView);

            blog_judul = listItemView.findViewById(R.id.blog_judul);
            blog_desc = listItemView.findViewById(R.id.blog_desc);
            blog_image = listItemView.findViewById(R.id.blog_image);
        }
    }
}
