package com.example.hearbuddy;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.hearbuddy.model.DisciplinaModel;

public class TelaDisciplina extends AppCompatActivity {
   ImageButton btdoc;
   ImageButton btcrono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_tela_disciplina );

        btdoc= (ImageButton) findViewById(R.id.btdoc);
        btcrono= (ImageButton) findViewById(R.id.btcrono);

        btdoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt1chamateladoc();
            }

            private void bt1chamateladoc() {
                Intent intentDocumento = getIntent();
                Intent intent = new Intent(TelaDisciplina.this, Documento.class);
                intent.putExtra("disciplinaSelecionada", (DisciplinaModel) intentDocumento.getSerializableExtra("disciplinaSelecionada"));
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
                intent.putExtra("disciplinaSelecionada", (DisciplinaModel) intentCorno.getSerializableExtra("disciplinaSelecionada"));
                startActivity(intent);

            }

        });


    }
    }

