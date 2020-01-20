package com.example.hearbuddy.model;

import java.io.Serializable;

public class DocumentoModel implements Serializable {
    private Long Id;
    private String nomeDocumento;
    private String textoDocumento;
    private DisciplinaModel disciplinaAssociada;

    public DisciplinaModel getDisciplinaAssociada() {
        return disciplinaAssociada;
    }

    public void setDisciplinaAssociada(DisciplinaModel disciplinaAssociada) {
        this.disciplinaAssociada = disciplinaAssociada;
    }

    public String getTextoDocumento() {
        return textoDocumento;
    }
    public void setTextoDocumento(String textoDocumento) {
        this.textoDocumento = textoDocumento;
    }

    public Long getId() {
        return Id;
    }
    public void setId(Long id) {
        Id = id;
    }

    public String getNomeDocumento() {
        return nomeDocumento;
    }
    public void setNomeDocumento(String nomeDocumento) {
        this.nomeDocumento = nomeDocumento;
    }

}
