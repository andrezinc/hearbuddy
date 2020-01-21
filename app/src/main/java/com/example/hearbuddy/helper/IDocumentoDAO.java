package com.example.hearbuddy.helper;

import com.example.hearbuddy.model.DisciplinaModel;
import com.example.hearbuddy.model.DocumentoModel;

import java.util.List;

public interface IDocumentoDAO {

    public boolean adicionar(DocumentoModel documentoModel);
    public boolean atualizar(DocumentoModel documentoModel);
    public boolean remover(DocumentoModel documentoModel);
    public List<DocumentoModel> listarDocumentos(DisciplinaModel disciplinaRecuperada);
}
