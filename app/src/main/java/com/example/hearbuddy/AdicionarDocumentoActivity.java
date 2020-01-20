package com.example.hearbuddy;
import android.content.Intent;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.hearbuddy.R;
import com.example.hearbuddy.helper.DocumentoDAO;
import com.example.hearbuddy.model.DisciplinaModel;
import com.example.hearbuddy.model.DocumentoModel;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class AdicionarDocumentoActivity extends AppCompatActivity {
    private TextInputEditText editDocumento;
    private DocumentoModel documentoAtual;
    private Intent intentDisciplina;
    private EditText editTextoDocumento;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_documento);
        editDocumento = findViewById(R.id.nomeDocumentoTextView);
        editTextoDocumento = findViewById(R.id.txtDocumento);
        documentoAtual = (DocumentoModel) getIntent().getSerializableExtra("documentoSelecionado");
        intentDisciplina = getIntent();
        filePath = intentDisciplina.getStringExtra("pathExterno");

        if(filePath!=null){
        try {
            String parsedText="";
            PdfReader reader = new PdfReader(filePath);
            int n = reader.getNumberOfPages();
            for (int i = 0; i <n ; i++) {
                parsedText   = parsedText+ PdfTextExtractor.getTextFromPage(reader, i+1).trim()+"\n";
            }

            editTextoDocumento.setText(parsedText);

            reader.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        }

        if(documentoAtual!=null){
            editDocumento.setText(documentoAtual.getNomeDocumento());
            editTextoDocumento.setText(documentoAtual.getTextoDocumento());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_adicionar_documento, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.itemSalvar:
                DocumentoDAO documentoDAO = new DocumentoDAO(getApplicationContext());

                if(documentoAtual!=null){
                    String nomeDocumento = editDocumento.getText().toString();
                    String textoDocumento = editTextoDocumento.getText().toString();
                    if(!nomeDocumento.isEmpty()){
                        DocumentoModel documento = new DocumentoModel();
                        documento.setNomeDocumento(nomeDocumento);
                        documento.setTextoDocumento(textoDocumento);
                        documento.setId(documentoAtual.getId());
                        if (documentoDAO.atualizar(documento)){
                            finish();
                            Log.i("DabberADD", documento.getTextoDocumento());
                        }
                    }
                }

                else{
                    String nomeDocumento = editDocumento.getText().toString();
                    String textoDocumento = editTextoDocumento.getText().toString();
                    if(!nomeDocumento.isEmpty()){
                        DocumentoModel documento = new DocumentoModel();
                        DisciplinaModel disciplinaRecuperada = new DisciplinaModel();
                        disciplinaRecuperada = (DisciplinaModel) intentDisciplina.getSerializableExtra("disciplinaSelecionada");
                        if(disciplinaRecuperada!=null){
                        documento.setDisciplinaAssociada(disciplinaRecuperada);
                        documento.setNomeDocumento(nomeDocumento);
                        documento.setTextoDocumento(textoDocumento);
                        documento.getDisciplinaAssociada().setId(disciplinaRecuperada.getId());
                        documentoDAO.adicionar(documento);
                        finish();}
                        else{
                            DisciplinaModel disciplinaRecuperadaExt = (DisciplinaModel) intentDisciplina.getSerializableExtra("disciplinaSelecionadaExt");
                            documento.setDisciplinaAssociada(disciplinaRecuperadaExt);
                            documento.setNomeDocumento(nomeDocumento);
                            documento.setTextoDocumento(textoDocumento);
                            documento.getDisciplinaAssociada().setId(disciplinaRecuperadaExt.getId());
                            documentoDAO.adicionar(documento);
                            finish();
                        }
                    }
                }
        }
        return super.onOptionsItemSelected(item);
    }
}