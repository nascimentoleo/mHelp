package com.ifma.appmhelp.processors;

import android.content.Context;

import com.ifma.appmhelp.models.Mensagem;

/**
 * Created by leo on 12/20/16.
 */

public interface ProcessadorDeStanzas {

    void processar(Context ctx, Mensagem mensagem) throws Exception;
}
