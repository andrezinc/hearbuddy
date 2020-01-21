package com.example.hearbuddy.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.hearbuddy.model.DisciplinaModel;
import com.example.hearbuddy.model.DocumentoModel;

import java.util.ArrayList;
import java.util.List;

public class DocumentoDAO implements IDocumentoDAO {

    private final SQLiteDatabase read;
    private final SQLiteDatabase write;

    public DocumentoDAO(Context context) {
        DbHelper db = new DbHelper(context);
        read = db.getReadableDatabase();
        write = db.getWritableDatabase();
    }

    @Override
    public boolean adicionar(DocumentoModel documentoModel) {
        ContentValues cv = new ContentValues();
        cv.put("nomeDocumento",documentoModel.getNomeDocumento());
        cv.put("textoDocumento",documentoModel.getTextoDocumento());
        cv.put("idDocumentoxDisciplina", documentoModel.getDisciplinaAssociada().getId());

        try{write.insert("tbDocumento", null, cv);}
        catch(Exception e){e.printStackTrace(); return false;}
        return true;
    }

    @Override
    public boolean atualizar(DocumentoModel documentoModel) {
        ContentValues cv = new ContentValues();
        cv.put("nomeDocumento", documentoModel.getNomeDocumento());
        cv.put("textoDocumento", documentoModel.getTextoDocumento());


        try{
            String[] args = {documentoModel.getId().toString()};
            write.update("tbDocumento", cv, "idDocumento=?", args);}
        catch(Exception e){e.printStackTrace(); return false;}
        return true;
    }



    @Override
    public boolean remover(DocumentoModel documentoModel) {
        try{
            String[] args = {documentoModel.getId().toString()};
            write.delete("tbDocumento", "idDocumento=?", args);}
        catch(Exception e){e.printStackTrace(); return false;}
        return true;
    }

    @Override
    public List<DocumentoModel> listarDocumentos(DisciplinaModel disciplinaRecuperada) {
        List<DocumentoModel> documentos = new ArrayList<>();
        String[] args = {disciplinaRecuperada.getId().toString()};
        Cursor c = read.rawQuery("SELECT * FROM tbDocumento WHERE idDocumentoxDisciplina = ?;", args);
        while(c.moveToNext()){
            DocumentoModel documento = new DocumentoModel();
            Long id = c.getLong(c.getColumnIndex("idDocumento"));
            String nomeDocumento = c.getString(c.getColumnIndex("nomeDocumento"));
            String textoDocumento = c.getString(c.getColumnIndex("textoDocumento"));
            documento.setId(id);
            documento.setNomeDocumento(nomeDocumento);
            documento.setTextoDocumento(textoDocumento);
            documento.setDisciplinaAssociada(disciplinaRecuperada);
            documentos.add(documento);
        }
        c.close();
        return documentos;
    }
}
