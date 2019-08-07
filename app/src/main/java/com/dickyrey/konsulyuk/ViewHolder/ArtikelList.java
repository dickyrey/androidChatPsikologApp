package com.dickyrey.konsulyuk.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.dickyrey.konsulyuk.Adapter.MessageAdapter;
import com.dickyrey.konsulyuk.ArtikelActivity;
import com.dickyrey.konsulyuk.Model.Artikel;
import com.dickyrey.konsulyuk.NewPostActivity;
import com.dickyrey.konsulyuk.ProfileActivity;
import com.dickyrey.konsulyuk.R;
import com.dickyrey.konsulyuk.SettingArtikel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ArtikelList extends RecyclerView.Adapter<ArtikelList.MyHolder>{


    private Context mContext;
    private List<Artikel> listArtikel;

    public ArtikelList(Context mContext, List<Artikel> listArtikel){
        this.listArtikel = listArtikel;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public ArtikelList.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artikel,parent,false);

        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ArtikelList.MyHolder holder, final int position) {
        final Artikel a = listArtikel.get(position);

        holder.blog_judul.setText(a.getJudul());
        holder.blog_topik.setText(a.getTopik());
        Picasso.get().load(a.getImage_thumb()).into(holder.blog_image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CharSequence options[] = new CharSequence[]
                        {
                                "Baca",
                                "Ubah",
                                "Hapus",
                                "Batal"
                        };
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("Pilih Aksi");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0){
                            Intent artikelIntent = new Intent(mContext, ArtikelActivity.class);
                            artikelIntent.putExtra("artikel_id", a.getArtikel_id());
                            artikelIntent.putExtra("topik", a.getTopik());
                            artikelIntent.putExtra("judul", a.getJudul());
                            artikelIntent.putExtra("desc", a.getDesc());
                            artikelIntent.putExtra("image_thumb", a.getImage_thumb());
                            artikelIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(artikelIntent);

                        }else if (i == 1){
                            Intent ubahIntent = new Intent(mContext, SettingArtikel.class);
                            ubahIntent.putExtra("artikel_id", a.getArtikel_id());
//                            ubahIntent.putExtra("topik", a.getTopik());
//                            ubahIntent.putExtra("judul", a.getJudul());
//                            ubahIntent.putExtra("desc", a.getDesc());
//                            ubahIntent.putExtra("image_thumb", a.getImage_thumb());
                            ubahIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(ubahIntent);

                        }else if (i == 2){
                            deleteArtikel(position, holder);
                        }
                    }
                });
                builder.show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return listArtikel.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        public TextView blog_judul, blog_topik;
        public ImageView blog_image;

        public MyHolder(@NonNull View listItemView) {
            super(listItemView);

            blog_judul = listItemView.findViewById(R.id.blog_judul);
            blog_topik = listItemView.findViewById(R.id.blog_topik);
            blog_image = listItemView.findViewById(R.id.blog_image);
        }
    }

    private void deleteArtikel(final int position, final ArtikelList.MyHolder holder){
        final DatabaseReference deleteRef = FirebaseDatabase.getInstance().getReference("Artikel");
        deleteRef
                .child(listArtikel.get(position).getArtikel_id())
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            listArtikel.clear();
                            holder.itemView.setVisibility(View.GONE);
                            Toast.makeText(mContext, "Berhasil dihapus", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
