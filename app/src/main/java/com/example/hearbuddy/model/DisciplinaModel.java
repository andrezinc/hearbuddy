package com.example.hearbuddy.model;

import java.io.Serializable;

public class DisciplinaModel implements Serializable {
    private Long id;
    private String nomeDisciplina;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeDisciplina() {
        return nomeDisciplina;
    }
    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }


}
