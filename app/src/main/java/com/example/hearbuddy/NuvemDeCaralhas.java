package com.example.hearbuddy;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.mordred.wordcloud.WordCloud;

import java.util.HashMap;
import java.util.Map;

public class NuvemDeCaralhas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuvem_de_caralhas);
        ImageView imgView = findViewById(R.id.imageView);


        String textoNuvem =  getIntent().getStringExtra("textoNuvem");
        Log.i("DabberNuvem", textoNuvem);

        String[] words = textoNuvem.split("\\W+");


        Map<String, Integer> nMap = new HashMap<>();

        for(int i=0;i<words.length;i++){
            nMap.put(words[i], i);
        }

        int myColor = getResources().getColor(R.color.colorPrimary);
        WordCloud wd = new WordCloud(nMap, 300, 400, myColor ,Color.BLACK);
        wd.setWordColorOpacityAuto(true);
        wd.setPaddingX(5);
        wd.setPaddingY(5);

        Bitmap generatedWordCloudBmp = wd.generate();

        imgView.setImageBitmap(generatedWordCloudBmp);
    }
}
