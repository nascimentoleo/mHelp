package com.ifma.appmhelp.enums;

import com.ifma.appmhelp.controls.ProcessadorDeMensagens;
import com.ifma.appmhelp.controls.ProcessadorDeSolicitacoes;

/**
 * Created by leo on 12/20/16.
 */

public enum TipoDeMensagem {
    SOLICITACAO_ROSTER(new ProcessadorDeSolicitacoes());

    private ProcessadorDeMensagens processador;

    TipoDeMensagem(ProcessadorDeMensagens processador) {
        this.processador = processador;
    }

    public ProcessadorDeMensagens getProcessador() {
        return processador;
    }
}
