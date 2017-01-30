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

    public ProntuarioParaEnvio getProntuarioParaEnvio() {
        return prontuarioParaEnvio;
    }

    public String toJson(){
        return new Gson().toJson(this);
    }

    public static PacienteParaEnvio fromJson(String jsonObject){
        return new Gson().fromJson(jsonObject, PacienteParaEnvio.class);
    }
}
