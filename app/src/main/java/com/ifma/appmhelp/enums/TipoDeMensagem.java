package com.ifma.appmhelp.enums;

import com.ifma.appmhelp.processors.ProcessadorDeMensagensOcorrencia;
import com.ifma.appmhelp.processors.ProcessadorDeStanzas;
import com.ifma.appmhelp.processors.ProcessadorDeOcorrencias;
import com.ifma.appmhelp.processors.ProcessadorDeProntuarios;
import com.ifma.appmhelp.processors.ProcessadorDeSolicitacoes;

/**
 * Created by leo on 12/20/16.
 */

public enum TipoDeMensagem {
    SOLICITACAO_ROSTER(new ProcessadorDeSolicitacoes()),
    ATUALIZACAO_PRONTUARIO(new ProcessadorDeProntuarios()),
    OCORRENCIA(new ProcessadorDeOcorrencias()),
    MENSAGEM_OCORRENCIA(new ProcessadorDeMensagensOcorrencia());

    private ProcessadorDeStanzas processador;

    TipoDeMensagem(ProcessadorDeStanzas processador) {
        this.processador = processador;
    }

    public ProcessadorDeStanzas getProcessador() {
        return processador;
    }
}
