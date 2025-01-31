package com.example.registro.ui.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.registro.R;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyHolder> {

    Context context;
    ArrayList<ModelClass> arrayList;
    LayoutInflater layoutInflater;

    public Adapter(Context context, ArrayList<ModelClass> arrayList){
        this.context = context;
        this.arrayList = arrayList;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){
        View view = layoutInflater.inflate(R.layout.item_file,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder,int position){
        holder.NombrePropiedad.setText(arrayList.get(position).getNombrePropiedad());
        holder.Descripcion.setText(arrayList.get(position).getDescripcion());
        holder.img.setImageResource(arrayList.get(position).getImg());

    }

    @Override
    public int getItemCount(){
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView NombrePropiedad, Descripcion;
        ImageView img;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            NombrePropiedad = itemView.findViewById(R.id.txt);
            Descripcion = itemView.findViewById(R.id.txt2);
            img = itemView.findViewById(R.id.img);

        }
    }
}
