package com.isma.inmobiliaria.ui.contratos;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.isma.inmobiliaria.R;
import com.isma.inmobiliaria.model.ContratoDetalleDto;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ContratosAdapter  extends RecyclerView.Adapter<ContratosAdapter.ViewHolder>{

    private List<ContratoDetalleDto> listaContratos;
    private Context context;
    private LayoutInflater inflater;


    public ContratosAdapter(List<ContratoDetalleDto> listaContratos, Context context) {
        this.listaContratos = listaContratos;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ContratosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_contrato, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContratosAdapter.ViewHolder holder, int position) {

        ContratoDetalleDto dto = listaContratos.get(position);

        holder.tvDireccionInmueble.setText(dto.getDireccionInmueble());
        holder.tvNombreInquilino.setText(dto.getNombreInquilino());
        holder.tvFechaDesde.setText(formatearFecha(dto.getFechaDesde()));
        holder.tvFechaHasta.setText(formatearFecha(dto.getFechaHasta()));
        NumberFormat formatear = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
        holder.tvMonto.setText(formatear.format(dto.getMonto()));
        holder.tvCantidadCuotas.setText(dto.getCantidadCuotas() + " Cuotas");

        if (dto.getVigente() == 1) {
            holder.chipVigente.setText("Vigente");
            holder.chipVigente.setChipBackgroundColorResource(R.color.green_light);
            holder.chipVigente.setTextColor(Color.parseColor("#1B5E20"));

        } else {
            holder.chipVigente.setText("Finalizado");
            holder.chipVigente.setChipBackgroundColorResource(R.color.grey_light); // (Debes crear este color)
            holder.chipVigente.setTextColor(Color.parseColor("#616161"));
        }


        holder.btnVerPagos.setOnClickListener(v -> {
            // Prepara el ID del contrato para enviarlo al siguiente fragmento
            Bundle bundle = new Bundle();
            bundle.putInt("idContrato", dto.getIdContrato());

            // Navega al Fragmento de Pagos
            // (Asegúrate de que 'action_contratosFragment_to_pagosFragment' exista en tu nav_graph.xml)
            Navigation.findNavController(v).navigate(R.id.action_contratosFragment_to_pagosFragment, bundle);

            // Toast.makeText(context, "Ver pagos del contrato: " + dto.getIdContrato(), Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return listaContratos == null ? 0 : listaContratos.size();
    }


    public void actualizarLista(List<ContratoDetalleDto> nuevaLista) {
        this.listaContratos = nuevaLista;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Chip chipVigente;
        TextView tvDireccionInmueble;
        TextView tvNombreInquilino;
        TextView tvFechaDesde;
        TextView tvFechaHasta;
        TextView tvMonto;
        TextView tvCantidadCuotas;
        Button btnVerPagos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chipVigente = itemView.findViewById(R.id.chipVigente);
            tvDireccionInmueble = itemView.findViewById(R.id.tvDireccionInmueble);
            tvNombreInquilino = itemView.findViewById(R.id.tvNombreInquilino);
            tvFechaDesde = itemView.findViewById(R.id.tvFechaDesde);
            tvFechaHasta = itemView.findViewById(R.id.tvFechaHasta);
            tvMonto = itemView.findViewById(R.id.tvMonto);
            tvCantidadCuotas = itemView.findViewById(R.id.tvCantidadCuotas);
            btnVerPagos = itemView.findViewById(R.id.btnVerPagos);
        }
    }

    private String formatearFecha(String fechaIso) {
        if (fechaIso == null) return "N/A";
        try {
            // Define el formato de ENTRADA (ISO)
            SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            // Define el formato de SALIDA (Legible)
            SimpleDateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            Date date = inputFormatter.parse(fechaIso);
            return outputFormatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return fechaIso; // Devuelve la fecha original si falla el parseo
        }
    }
}
