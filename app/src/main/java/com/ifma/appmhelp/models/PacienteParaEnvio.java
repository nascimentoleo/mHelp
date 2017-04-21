package com.ifma.appmhelp.models;

import com.google.gson.Gson;

/**
 * Created by leo on 1/30/17.
 */

public class PacienteParaEnvio {
    private Paciente paciente;
    private ProntuarioParaEnvio prontuarioParaEnvio;


    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public void setProntuarioParaEnvio(ProntuarioParaEnvio prontuarioParaEnvio) {
        this.prontuarioParaEnvio = prontuarioParaEnvio;
    }

    public ProntuarioParaEnvio getProntuarioParaEnvio() {
        return prontuarioParaEnvio;
    }

    public String toJson(){
        return new Gson().toJson(this);
    }

    public static PacienteParaEnvio fromJson(String jsonObject){
        return new Gson().fromJson(jsonObject, PacienteParaEnvio.class);
    }

    public void preparaPacienteParaEnvio(){
        this.getPaciente().setId(null);
        this.getPaciente().getUsuario().setId(null);
        this.getPaciente().getUsuario().setSenha(null);
        this.getPaciente().setProntuario(null);
    }

}
