package com.example.hearbuddy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hearbuddy.R;
import com.example.hearbuddy.model.DocumentoModel;

import java.util.List;

public class AdaptadorDocumento extends RecyclerView.Adapter<AdaptadorDocumento.MyViewHolder> {

    private List<DocumentoModel> listaDocumentos;

    public AdaptadorDocumento(List<DocumentoModel> listaDocumento) {
        this.listaDocumentos = listaDocumento;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View viewItemListaDocumento = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.lista_documento_adapter,viewGroup, false);
        return new MyViewHolder(viewItemListaDocumento);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        DocumentoModel documentoModel = listaDocumentos.get(i);
        myViewHolder.textDocumento.setText(documentoModel.getNomeDocumento());
    }

    @Override
    public int getItemCount() {
        return this.listaDocumentos.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textDocumento;
        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            textDocumento = itemView.findViewById(R.id.textViewDocumento);
        }
    }
}
