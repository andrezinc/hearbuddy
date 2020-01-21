package com.example.hearbuddy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.hearbuddy.model.DisciplinaModel;

public class MainActivity extends AppCompatActivity {
    private ImageButton btdisc;
    private ImageButton btcronoH;
    private ImageButton btaudio;

    private static final int RECORD_AUDIO = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);

        }

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO},
                    RECORD_AUDIO);

        }



        btdisc = findViewById(R.id.btdisc);
        btcronoH = findViewById(R.id.btcronoH);
        btaudio = findViewById(R.id.btaudio2);



        btdisc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt1chamateladisciplina();
            }

            private void bt1chamateladisciplina() {
                startActivity(new Intent(MainActivity.this, Disciplina.class));
            }

        });

        btcronoH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btcronoH();
            }

            private void btcronoH() {
                startActivity(new Intent(MainActivity.this, CronogramaHome.class));
            }

        });

        btaudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btaudioc();
            }

            private void btaudioc() {
                Intent intentAudio = getIntent();
                Intent intent = new Intent(MainActivity.this, GravadorAudio.class);
                intent.putExtra("disciplinaSelecionada", intentAudio.getSerializableExtra("disciplinaSelecionada"));
                startActivity(intent);

            }

        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1001:{if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Aceito", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(this, "Não aceito", Toast.LENGTH_SHORT).show();
                finish();
            }


        }
            case  2000:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                }else {
                    Toast.makeText(this, "Permitir acesso de áudio", Toast.LENGTH_SHORT).show();
                }
    }

    }
}
