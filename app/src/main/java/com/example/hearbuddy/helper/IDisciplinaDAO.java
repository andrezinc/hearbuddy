package com.example.hearbuddy.helper;
import com.example.hearbuddy.model.DisciplinaModel;
import java.util.List;

public interface IDisciplinaDAO {

    public boolean adicionar(DisciplinaModel disciplinaModel);
    public boolean atualizar(DisciplinaModel disciplinaModel);
    public boolean remover(DisciplinaModel disciplinaModel);
    public List<DisciplinaModel> listarDisciplinas();
}
