package com.example.hearbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hearbuddy.helper.DocumentoDAO;
import com.example.hearbuddy.model.DocumentoModel;

import java.util.ArrayList;
import java.util.Locale;

public class TelaDocumento extends AppCompatActivity {
    private EditText editDocumento;
    private ImageButton btCriarNuvem;
    private ImageButton btTextToSpeech;
    private ImageButton btSalvarDocumento;
    private DocumentoModel documentoAtual;
    private TextToSpeech textToSpeech;
    private String textoDaNuvem = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_documento);
        editDocumento = findViewById(R.id.tvTextoDocumento);
        btTextToSpeech = findViewById(R.id.btTextToSpeech);
        btSalvarDocumento = findViewById(R.id.btSalvarDocumento);
        btTextToSpeech = findViewById(R.id.btTextToSpeech);
        btCriarNuvem = findViewById(R.id.btCriarNuvem);


        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.getDefault());
                }
            }
        });

                documentoAtual = (DocumentoModel) getIntent().getSerializableExtra("documentoSelecionado");


        editDocumento.setText(documentoAtual.getTextoDocumento());


        btSalvarDocumento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizar();
            }
        });

        btTextToSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("blopers", "falando");
                String fala = editDocumento.getText().toString();

                int limiteDivisao = 3900;
                if(fala.length() >= limiteDivisao) {
                    int tamanhoTexto = fala.length();
                    ArrayList<String> falas = new ArrayList<String>();
                    int cont = tamanhoTexto / limiteDivisao + ((tamanhoTexto % limiteDivisao == 0) ? 0 : 1);
                    int start = 0;
                    int end = fala.indexOf(" ", limiteDivisao);
                    for(int i = 1; i<=cont; i++) {
                        falas.add(fala.substring(start, end));
                        start = end;
                        if((start + limiteDivisao) < tamanhoTexto) {
                            end = fala.indexOf(" ", start + limiteDivisao);
                        } else {
                            end = tamanhoTexto;
                        }
                    }
                    for(int i=0; i<falas.size(); i++) {
                        textToSpeech.speak(falas.get(i), TextToSpeech.QUEUE_ADD, null);
                    }
                } else {
                    textToSpeech.speak(fala, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        if(textoDaNuvem.length()>1000){btCriarNuvem.setVisibility(View.INVISIBLE);}



        btCriarNuvem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaDocumento.this, NuvemDeCaralhas.class);
                intent.putExtra("textoNuvem", textoDaNuvem);
                startActivity(intent);
            }
        });



        editDocumento.setOnLongClickListener(new View.OnLongClickListener() {
                                                 public boolean onLongClick(View v) {
                                                     int min = 0;
                                                     int max = editDocumento.getText().length();

                                                     if (editDocumento.isFocused()) {
                                                         final int comeco = editDocumento.getSelectionStart();
                                                         final int fim = editDocumento.getSelectionEnd();
                                                         min = Math.max(0, Math.min(comeco, fim));
                                                         max = Math.max(0, Math.max(comeco, fim));
                                                     }
                                                     final CharSequence bodegaSelecionada = editDocumento.getText().subSequence(min, max);
                                                     textoDaNuvem = textoDaNuvem + " " + bodegaSelecionada.toString();
                                                     return true;
                                                 }
                                             });

/*        editDocumento.clearFocus();
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(editDocumento.getWindowToken(), 0);
            }
*/






        }



    public void onPause(){
        if(textToSpeech!=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();


    }



    public void atualizar(){
        DocumentoDAO documentoDAO = new DocumentoDAO(getApplicationContext());
        String textoDocumentox = editDocumento.getText().toString();
        if(!textoDocumentox.isEmpty()) {
            DocumentoModel documento = new DocumentoModel();
            documento.setTextoDocumento(textoDocumentox);
            documento.setNomeDocumento(documentoAtual.getNomeDocumento());
            documento.setId(documentoAtual.getId());
            documentoDAO.atualizar(documento);
            editDocumento.setText(documento.getTextoDocumento());
            Log.i("DabberEdit", editDocumento.getText().toString());
            Log.i("DabberDoc", documento.getTextoDocumento());
            Log.i("DabberDoc", documento.getId().toString());
            Log.i("DabberDoc", documento.getNomeDocumento());
            finish();



        }
    }

    }


