package com.ifma.appmhelp.controls;

import android.content.Context;

/**
 * Created by leo on 11/29/16.
 */
public abstract class BaseController{

    protected String msgErro;
    protected Context ctx;

    public BaseController(Context ctx) {
        this.ctx = ctx;
    }

    public String getMsgErro() {
        return msgErro;
    }
}
