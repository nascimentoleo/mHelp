package com.ifma.appmhelp.enums;

import com.ifma.appmhelp.controls.ProcessadorDeMensagens;
import com.ifma.appmhelp.controls.ProcessadorDeOcorrencias;
import com.ifma.appmhelp.controls.ProcessadorDeProntuarios;
import com.ifma.appmhelp.controls.ProcessadorDeSolicitacoes;

/**
 * Created by leo on 12/20/16.
 */

public enum TipoDeMensagem {
    SOLICITACAO_ROSTER(new ProcessadorDeSolicitacoes()),
    ATUALIZACAO_PRONTUARIO(new ProcessadorDeProntuarios()),
    NOVA_OCORRENCIA(new ProcessadorDeOcorrencias());

    private ProcessadorDeMensagens processador;

    TipoDeMensagem(ProcessadorDeMensagens processador) {
        this.processador = processador;
    }

    public ProcessadorDeMensagens getProcessador() {
        return processador;
    }
}
