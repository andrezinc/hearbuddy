package com.example.hearbuddy;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.View;
import android.widget.AdapterView;
import com.example.hearbuddy.adapter.AdaptadorDisciplina;
import com.example.hearbuddy.helper.DbHelper;
import com.example.hearbuddy.helper.DisciplinaDAO;
import com.example.hearbuddy.helper.RecyclerItemClickListener;
import com.example.hearbuddy.model.DisciplinaModel;

import java.util.ArrayList;
import java.util.List;

public class Disciplina extends AppCompatActivity {
    private RecyclerView recyclerViewDisciplina;
    private AdaptadorDisciplina disciplinaAdapter;
    private List<DisciplinaModel> listaDisciplinas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disciplina);
        recyclerViewDisciplina = findViewById(R.id.recyclerDisciplina);

        recyclerViewDisciplina.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerViewDisciplina,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                DisciplinaModel disciplinaSelecionada = listaDisciplinas.get(position);
                                Intent intent = new Intent(Disciplina.this, TelaDisciplina.class);
                                intent.putExtra("disciplinaSelecionada", disciplinaSelecionada);
                                startActivity(intent);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                DisciplinaModel disciplinaSelecionada = listaDisciplinas.get(position);
                                Intent intent = new Intent(Disciplina.this, AdicionarDisciplinaActivity.class);
                                intent.putExtra("disciplinaSelecionada", disciplinaSelecionada);
                                startActivity(intent);
                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }

                        })
        );
        FloatingActionButton fab = findViewById(R.id.btAdicionarDisciplina);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdicionarDisciplinaActivity.class);
                startActivity(intent);
            }
        });
    }

    public void carregarListaDisciplinas(){
        DisciplinaDAO disciplinaDAO = new DisciplinaDAO(getApplicationContext());
        listaDisciplinas = disciplinaDAO.listarDisciplinas();
        disciplinaAdapter = new AdaptadorDisciplina(listaDisciplinas);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewDisciplina.setLayoutManager(layoutManager);
        recyclerViewDisciplina.setHasFixedSize(true);
        recyclerViewDisciplina.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerViewDisciplina);
        recyclerViewDisciplina.setAdapter(disciplinaAdapter);
    }

    ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {

             AlertDialog.Builder dialog = new AlertDialog.Builder(Disciplina.this);
             dialog.setTitle("Confirmar exclusão")
             .setMessage("Deseja realmente excluir a disciplina e seus documentos?" )
                     .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int which) {
                    DisciplinaModel disciplinaSelecionada = listaDisciplinas.get(viewHolder.getAdapterPosition());
                    DisciplinaDAO disciplinaDAO = new DisciplinaDAO(getApplicationContext());
                    DbHelper dbHelper = new DbHelper(getApplicationContext());
                    disciplinaDAO.remover(disciplinaSelecionada);
                    listaDisciplinas.remove(disciplinaSelecionada);
                    String[] args = {disciplinaSelecionada.getId().toString()};
                    dbHelper.getWritableDatabase().delete("tbDocumento", "idDocumentoxDisciplina=?", args);
                             dbHelper.getWritableDatabase().delete("tbCronograma", "idCronogramaxDisciplina=?", args);
                    disciplinaAdapter.notifyDataSetChanged();
                         }
            });

            dialog.setNegativeButton("Não", null );
            dialog.create();
            dialog.show();
            disciplinaAdapter.notifyDataSetChanged();

        }
    };

    @Override
    protected void onStart() {
        carregarListaDisciplinas();
        super.onStart();
    }
}