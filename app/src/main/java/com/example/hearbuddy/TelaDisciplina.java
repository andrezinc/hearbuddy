package com.example.hearbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hearbuddy.model.DisciplinaModel;

public class TelaDisciplina extends AppCompatActivity {
   private ImageButton btdoc;
   private ImageButton btcrono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_tela_disciplina );

        btdoc= findViewById(R.id.btdoc);
        btcrono= findViewById(R.id.btcrono);

        btdoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt1chamateladoc();
            }

            private void bt1chamateladoc() {
                Intent intentDocumento = getIntent();
                Intent intent = new Intent(TelaDisciplina.this, Documento.class);
                intent.putExtra("disciplinaSelecionada", intentDocumento.getSerializableExtra("disciplinaSelecionada"));
                startActivity(intent);
            }

        });



        btcrono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btcorno();
            }

            private void btcorno() {
                Intent intentCorno = getIntent();
                Intent intent = new Intent(TelaDisciplina.this, CronogramaDisciplina.class);
                intent.putExtra("disciplinaSelecionada", intentCorno.getSerializableExtra("disciplinaSelecionada"));
                startActivity(intent);

            }

        });


    }
    }

