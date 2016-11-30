package com.ifma.appmhelp.controls;

import android.content.Context;

/**
 * Created by leo on 11/29/16.
 */
public abstract class BaseController{

    String msgErro;
    Context ctx;

    public BaseController(Context ctx) {
        this.ctx = ctx;
    }

    public String getMsgErro() {
        return msgErro;
    }

    void setMsgErro(String msgErro) {
        this.msgErro = msgErro;
    }

}
