package com.ifma.appmhelp.controls;

import com.ifma.appmhelp.models.Usuario;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

/**
 * Created by leo on 11/12/16.
 */
public class Login {

    private String msgErro;

    public String getMsgErro() {
        return msgErro;
    }

    public boolean realizaLogin(Usuario usuario, AbstractXMPPConnection conexao){
        try {
            conexao.login(usuario.getLogin(), usuario.getSenha());
            return true;
        }catch (IOException | SmackException | XMPPException e) {
            e.printStackTrace();
            this.msgErro = e.getMessage();
            return false;
        }
    }
}
