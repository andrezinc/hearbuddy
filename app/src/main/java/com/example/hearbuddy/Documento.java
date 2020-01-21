package com.example.hearbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hearbuddy.adapter.AdaptadorDocumento;
import com.example.hearbuddy.helper.DocumentoDAO;
import com.example.hearbuddy.helper.RecyclerItemClickListener;
import com.example.hearbuddy.model.DisciplinaModel;
import com.example.hearbuddy.model.DocumentoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Documento extends AppCompatActivity {
    private RecyclerView recyclerViewDocumento;
    private AdaptadorDocumento documentoAdapter;
    private List<DocumentoModel> listaDocumentos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documento);
        recyclerViewDocumento = findViewById(R.id.recyclerDocumento);
        ImageButton arquivoInterno = findViewById(R.id.btArmazenamentoInterno);

        recyclerViewDocumento.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerViewDocumento,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                DocumentoModel documentoSelecionado = listaDocumentos.get(position);
                                Intent intent = new Intent(Documento.this, TelaDocumento.class);
                                intent.putExtra("documentoSelecionado", documentoSelecionado);
                                startActivity(intent);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                DocumentoModel documentoSelecionado = listaDocumentos.get(position);
                                Intent intent = new Intent(Documento.this, AdicionarDocumentoActivity.class);
                                intent.putExtra("documentoSelecionado", documentoSelecionado);
                                startActivity(intent);
                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }

                        })
        );
        FloatingActionButton fab = findViewById(R.id.btAdicionarDocumento);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentTelaDisciplina = getIntent();
                Intent intent = new Intent(getApplicationContext(), AdicionarDocumentoActivity.class);
                intent.putExtra("disciplinaSelecionada", intentTelaDisciplina.getSerializableExtra("disciplinaSelecionada"));
                startActivity(intent);
            }
        });

        arquivoInterno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialFilePicker()
                        .withActivity(Documento.this)
                        .withRequestCode(1000)
                        .withFilter(Pattern.compile(".*\\.pdf$")) // Filtering files and directories by file name using regexp
                        .withFilterDirectories(false) // Set directories filterable (false by default)
                        .withHiddenFiles(true) // Show hidden files and folders
                        .start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK) {
            Intent intentTelaDisciplina = getIntent();
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            Intent intentExternal = new Intent(getApplicationContext(), AdicionarDocumentoActivity.class);
            intentExternal.putExtra("pathExterno", filePath);
            intentExternal.putExtra("disciplinaSelecionadaExt", intentTelaDisciplina.getSerializableExtra("disciplinaSelecionada"));
            startActivity(intentExternal);
        }
    }

    private void carregarListaDocumentos(){
        Intent intentTelaDisciplina = getIntent();
        DocumentoDAO documentoDAO = new DocumentoDAO(getApplicationContext());
        listaDocumentos = documentoDAO.listarDocumentos((DisciplinaModel) intentTelaDisciplina.getSerializableExtra("disciplinaSelecionada"));
        documentoAdapter = new AdaptadorDocumento(listaDocumentos);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewDocumento.setLayoutManager(layoutManager);
        recyclerViewDocumento.setHasFixedSize(true);
        recyclerViewDocumento.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerViewDocumento);
        recyclerViewDocumento.setAdapter(documentoAdapter);
    }

    private final ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            DocumentoDAO documentoDAO = new DocumentoDAO(getApplicationContext());
            documentoDAO.remover(listaDocumentos.get(viewHolder.getAdapterPosition()));
            listaDocumentos.remove(viewHolder.getAdapterPosition());
            documentoAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onStart() {
        carregarListaDocumentos();
        super.onStart();
    }
}
