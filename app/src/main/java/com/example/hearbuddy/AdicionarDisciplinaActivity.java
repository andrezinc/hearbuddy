package com.example.hearbuddy;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hearbuddy.helper.DisciplinaDAO;
import com.example.hearbuddy.model.DisciplinaModel;
import com.google.android.material.textfield.TextInputEditText;

public class AdicionarDisciplinaActivity extends AppCompatActivity {
    private TextInputEditText editDisciplina;
    private DisciplinaModel disciplinaAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_disciplina);
        editDisciplina = findViewById(R.id.nomeDisciplinaTextView);
        disciplinaAtual = (DisciplinaModel) getIntent().getSerializableExtra("disciplinaSelecionada");

        if(disciplinaAtual!=null){
            editDisciplina.setText(disciplinaAtual.getNomeDisciplina());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_adicionar_disciplina, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.itemSalvar:
                DisciplinaDAO disciplinaDAO = new DisciplinaDAO(getApplicationContext());

                if(disciplinaAtual!=null){
                    String nomeDisciplina = editDisciplina.getText().toString();
                    if(!nomeDisciplina.isEmpty()){
                        DisciplinaModel disciplina = new DisciplinaModel();
                        disciplina.setNomeDisciplina(nomeDisciplina);
                        disciplina.setId(disciplinaAtual.getId());

                        if (disciplinaDAO.atualizar(disciplina)){
                            finish();
                        }
                    }
                }

                else{
                    String nomeDisciplina = editDisciplina.getText().toString();
                    if(!nomeDisciplina.isEmpty()){
                        DisciplinaModel disciplina = new DisciplinaModel();
                        disciplina.setNomeDisciplina(nomeDisciplina);
                        disciplinaDAO.adicionar(disciplina);
                        finish();}
                }
        }
        return super.onOptionsItemSelected(item);
    }
}