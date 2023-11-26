package com.example.reto8.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reto8.R;
import com.example.reto8.VerActivity;
import com.example.reto8.entidades.Empresas;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListaEmpresasAdapter extends RecyclerView.Adapter<ListaEmpresasAdapter.EmpresaViewHolder> {

    ArrayList<Empresas> listaEmpresas;
    ArrayList<Empresas> listaOriginal;
    public ListaEmpresasAdapter(ArrayList<Empresas> listaEmpresas){
        this.listaEmpresas = listaEmpresas;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(listaEmpresas);
    }
    @NonNull
    @Override
    public EmpresaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_empresas, null, false);
        return new EmpresaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmpresaViewHolder holder, int position) {
        holder.viewNombre.setText(listaEmpresas.get(position).getNombre());
        holder.viewUrl.setText(listaEmpresas.get(position).getUrl());
        holder.viewProductos.setText(listaEmpresas.get(position).getProducto());
    }

    public void filtrado(final String txtBuscar){
        int longitud = txtBuscar.length();
        if (longitud == 0){
            listaEmpresas.clear();
            listaEmpresas.addAll(listaOriginal);
        }else{
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<Empresas> colection= listaEmpresas.stream()
                        .filter(i -> i.getNombre().toLowerCase().contains(txtBuscar.toLowerCase()))
                        .collect(Collectors.toList());
                listaEmpresas.clear();
                listaEmpresas.addAll(colection);

            }else{
                for (Empresas e: listaOriginal) {
                    if(e.getNombre().toLowerCase().contains(txtBuscar.toLowerCase())){
                        listaEmpresas.add(e);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listaEmpresas.size();
    }

    public class EmpresaViewHolder extends RecyclerView.ViewHolder {
        TextView viewNombre, viewUrl, viewTelefono, viewProductos;
        public EmpresaViewHolder(@NonNull View itemView) {
            super(itemView);
            viewNombre = itemView.findViewById(R.id.viewNombre);
            viewUrl = itemView.findViewById(R.id.viewUrl);
            viewProductos = itemView.findViewById(R.id.viewProductos);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent =new Intent(context, VerActivity.class);
                    intent.putExtra("ID", listaEmpresas.get(getAdapterPosition()).getId());
                    context.startActivity(intent);
                }
            });
        }
    }
}
