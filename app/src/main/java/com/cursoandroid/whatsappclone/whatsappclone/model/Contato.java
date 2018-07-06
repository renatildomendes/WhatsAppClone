package com.cursoandroid.whatsappclone.whatsappclone.model;

import com.cursoandroid.whatsappclone.whatsappclone.helper.Preferencias;

/**
 * Created by Renato on 05/07/2018.
 */

public class Contato {

    private String identificadorContato;
    private String nome;
    private String email;

    public Contato() {
    }

    public String getIdentificadorContato() {
        return identificadorContato;
    }

    public void setIdentificadorContato(String identificadorContato) {
        this.identificadorContato = identificadorContato;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
