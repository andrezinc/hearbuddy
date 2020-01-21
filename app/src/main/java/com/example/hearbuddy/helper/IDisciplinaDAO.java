package com.example.hearbuddy.helper;

import com.example.hearbuddy.model.DisciplinaModel;

import java.util.List;

interface IDisciplinaDAO {

    boolean adicionar(DisciplinaModel disciplinaModel);
    boolean atualizar(DisciplinaModel disciplinaModel);
    boolean remover(DisciplinaModel disciplinaModel);
    List<DisciplinaModel> listarDisciplinas();
}
