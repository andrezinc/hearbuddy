package com.example.hearbuddy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hearbuddy.R;
import com.example.hearbuddy.model.DisciplinaModel;

import java.util.List;

public class AdaptadorDisciplina extends RecyclerView.Adapter<AdaptadorDisciplina.MyViewHolder> {

    private List<DisciplinaModel> listaDisciplinas;

    public AdaptadorDisciplina(List<DisciplinaModel> listaDisciplina) {
        this.listaDisciplinas = listaDisciplina;

    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View viewItemListaDisciplina = LayoutInflater.from(viewGroup.getContext())
                                                     .inflate(R.layout.lista_disciplina_adapter,viewGroup, false);
        return new MyViewHolder(viewItemListaDisciplina);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        DisciplinaModel disciplinaModel = listaDisciplinas.get(i);
        myViewHolder.textDisciplina.setText(disciplinaModel.getNomeDisciplina());
    }

    @Override
    public int getItemCount() {
        return this.listaDisciplinas.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textDisciplina;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            textDisciplina = itemView.findViewById(R.id.textViewDocumento);
        }
    }
}
