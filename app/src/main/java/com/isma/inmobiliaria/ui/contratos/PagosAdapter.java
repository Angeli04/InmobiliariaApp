package com.isma.inmobiliaria.ui.contratos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isma.inmobiliaria.R;
import com.isma.inmobiliaria.model.Pago;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PagosAdapter extends RecyclerView.Adapter<PagosAdapter.ViewHolder> {

    private List<Pago> listaPagos;
    private Context context;
    private LayoutInflater inflater;


    public PagosAdapter(List<Pago> listaPagos, Context context) {
        this.listaPagos = listaPagos;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_pago, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pago pago = listaPagos.get(position);

        holder.tvNunmeroCuota.setText(String.valueOf(pago.getNumeroCuota()));
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
        holder.tvImportePago.setText(formatter.format(pago.getImporte()));
        holder.tvFechaPago.setText("Pagado el " + formatearFecha(pago.getFechaPago()));
        holder.tvConcepto.setText(pago.getConcepto());
        holder.tvMesPago.setText(formatearMesAnio(pago.getMesPago(), pago.getFechaPago()));
    }


    @Override
    public int getItemCount() {
        return listaPagos == null ? 0 : listaPagos.size();
    }

    public void actualizarLista(List<Pago> listaPagos){
        this.listaPagos = listaPagos;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvNunmeroCuota, tvImportePago, tvFechaPago, tvConcepto, tvMesPago;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNunmeroCuota = itemView.findViewById(R.id.tvNumeroCuota);
            tvImportePago = itemView.findViewById(R.id.tvImportePago);
            tvFechaPago = itemView.findViewById(R.id.tvFechaPago);
            tvConcepto = itemView.findViewById(R.id.tvConcepto);
            tvMesPago = itemView.findViewById(R.id.tvMesPago);
        }
    }

    private String formatearFecha(String fechaIso) {
        if (fechaIso == null) return "N/A";
        try {
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = input.parse(fechaIso);
            return output.format(date);
        } catch (ParseException e) {
            return fechaIso;
        }
    }

    private String formatearMesAnio(int mes, String fechaIso) {
        if (fechaIso == null) return "N/A";
        try {

            String[] nombresMeses = {
                    "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                    "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
            };


            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date date = input.parse(fechaIso);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int anio = cal.get(Calendar.YEAR);
            String nombreMes = nombresMeses[mes - 1];

            return nombreMes + " " + anio;
        } catch (Exception e) {
            return "Mes " + mes;
        }
    }

}
