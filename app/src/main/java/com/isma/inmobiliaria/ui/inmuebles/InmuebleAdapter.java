/*

package com.isma.inmobiliaria.ui.inmuebles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.isma.inmobiliaria.R;
import com.isma.inmobiliaria.model.Inmueble;

import java.util.List;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.InmuebleViewHolder> {

    private List<Inmueble> inmuebleList;

    private Context context;

    public InmuebleAdapter(List<Inmueble> inmuebleList, Context context) {
        this.inmuebleList = inmuebleList;
        this.context = context;
    }

    @NonNull
    @Override
    public InmuebleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.inmueble_card, parent, false);
        return new InmuebleViewHolder(vista);

    }

    @Override
    public void onBindViewHolder(@NonNull InmuebleViewHolder holder, int position) {
        String urls = "http://192.168.0.7:5164";
        Inmueble i = inmuebleList.get(position);
        holder.tvDireccion.setText//(i.getDireccion());
        holder.tvPrecio.setText(i.getPrecio());
        Glide.with(context)
                .load(urls + i.getImagen())
                .placeholder(null)
                .error("null")
                .into(holder.imagen);


    }

    @Override
    public int getItemCount() {
        return inmuebleList.size();
    }


    public class InmuebleViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDireccion, tvPrecio;
        private ImageView imagen;




        public InmuebleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            imagen = itemView.findViewById(R.id.imageView3);

        }
    }

}
*/