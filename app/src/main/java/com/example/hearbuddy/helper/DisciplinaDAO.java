package com.example.hearbuddy.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.hearbuddy.model.DisciplinaModel;

import java.util.ArrayList;
import java.util.List;

public class DisciplinaDAO implements IDisciplinaDAO {

    private final SQLiteDatabase read;
    private final SQLiteDatabase write;

    public DisciplinaDAO(Context context) {
        DbHelper db = new DbHelper(context);
        read = db.getReadableDatabase();
        write = db.getWritableDatabase();
    }

    @Override
    public boolean adicionar(DisciplinaModel disciplinaModel) {
        ContentValues cv = new ContentValues();
        cv.put("nomeDisciplina",disciplinaModel.getNomeDisciplina());

        try{write.insert("tbDisciplina", null, cv);}
        catch(Exception e){e.printStackTrace(); return false;}
        return true;
    }

    @Override
    public boolean atualizar(DisciplinaModel disciplinaModel) {
        ContentValues cv = new ContentValues();
        cv.put("nomeDisciplina", disciplinaModel.getNomeDisciplina());

        try{
            String[] args = {disciplinaModel.getId().toString()};
            write.update("tbDisciplina", cv, "idDisciplina=?", args);}
        catch(Exception e){e.printStackTrace(); return false;}
        return true;
    }

    @Override
    public boolean remover(DisciplinaModel disciplinaModel) {
        try{
            String[] args = {disciplinaModel.getId().toString()};
            write.delete("tbDisciplina", "idDisciplina=?", args);}
        catch(Exception e){e.printStackTrace(); return false;}
        return true;
    }

    @Override
    public List<DisciplinaModel> listarDisciplinas() {
        List<DisciplinaModel> disciplinas = new ArrayList<>();
        Cursor c = read.rawQuery("SELECT * FROM tbDisciplina;", null);
            while(c.moveToNext()){
                DisciplinaModel disciplina = new DisciplinaModel();
                Long id = c.getLong(c.getColumnIndex("idDisciplina"));
                String nomeDisciplina = c.getString(c.getColumnIndex("nomeDisciplina"));
                disciplina.setId(id);
                disciplina.setNomeDisciplina(nomeDisciplina);
                disciplinas.add(disciplina);
            }
        c.close();
        return disciplinas;
    }
}
