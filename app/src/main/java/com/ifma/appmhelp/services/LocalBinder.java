package com.ifma.appmhelp.services;

import android.os.Binder;

import java.io.Serializable;

/**
 * Created by leo on 1/18/17.
 */

public class LocalBinder extends Binder implements Serializable {

    ConexaoXMPPService service;

    public LocalBinder(ConexaoXMPPService service) {
        this.service = service;
    }

    public ConexaoXMPPService getService() {
        return service;
    }
}
