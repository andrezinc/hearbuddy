package com.example.hearbuddy.helper;

import com.example.hearbuddy.model.DisciplinaModel;
import com.example.hearbuddy.model.DocumentoModel;

import java.util.List;

interface IDocumentoDAO {

    boolean adicionar(DocumentoModel documentoModel);
    boolean atualizar(DocumentoModel documentoModel);
    boolean remover(DocumentoModel documentoModel);
    List<DocumentoModel> listarDocumentos(DisciplinaModel disciplinaRecuperada);
}
