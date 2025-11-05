package com.isma.inmobiliaria.ui.inquilinos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isma.inmobiliaria.R;
// Importamos el DTO que creamos
import com.isma.inmobiliaria.model.InquilinoAlquilerDto;

import java.util.List;

/**
 * Adaptador para el RecyclerView de Inquilinos.
 * Conecta la List<InquilinoAlquilerDto> con el layout item_inquilino.xml.
 */
public class InquilinosAdapter extends RecyclerView.Adapter<InquilinosAdapter.ViewHolder> {

    private List<InquilinoAlquilerDto> inquilinoList;
    private Context context;
    private LayoutInflater inflater;

    public InquilinosAdapter(List<InquilinoAlquilerDto> inquilinoList, Context context) {
        this.inquilinoList = inquilinoList;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el XML (item_inquilino.xml) que diseñamos
        View view = inflater.inflate(R.layout.item_inquilino, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Obtiene el inquilino de la posición actual
        InquilinoAlquilerDto inquilino = inquilinoList.get(position);

        // Combina Apellido y Nombre
        String nombreCompleto = inquilino.getApellido() + ", " + inquilino.getNombre();

        // Rellena los TextViews del ViewHolder
        holder.tvNombre.setText(nombreCompleto);
        holder.tvDni.setText(inquilino.getDni());
        holder.tvTelefono.setText(inquilino.getTelefono());
        holder.tvEmail.setText(inquilino.getEmail());
        holder.tvDireccion.setText(inquilino.getDireccionInmueble());
    }

    @Override
    public int getItemCount() {
        return inquilinoList.size();
    }

    /**
     * Método para actualizar la lista del adaptador (para el LiveData)
     */
    public void actualizarLista(List<InquilinoAlquilerDto> nuevaLista) {
        this.inquilinoList = nuevaLista;
        notifyDataSetChanged(); // Notifica al RecyclerView que los datos cambiaron
    }


    /**
     * ViewHolder: Mantiene las referencias a las vistas (TextViews)
     * de un solo item_inquilino.xml
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDni, tvTelefono, tvEmail, tvDireccion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Busca los IDs del XML (item_inquilino.xml)
            tvNombre = itemView.findViewById(R.id.tvInquilinoNombre);
            tvDni = itemView.findViewById(R.id.tvInquilinoDni);
            tvTelefono = itemView.findViewById(R.id.tvInquilinoTelefono);
            tvEmail = itemView.findViewById(R.id.tvInquilinoEmail);
            tvDireccion = itemView.findViewById(R.id.tvInquilinoDireccion);

            // Aquí es donde añadiríamos el setOnClickListener
            // si quisiéramos ir a un detalle del inquilino
            // itemView.setOnClickListener(...);
        }
    }
}

