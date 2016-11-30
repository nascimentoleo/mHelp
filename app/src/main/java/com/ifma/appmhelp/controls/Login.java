package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.Usuario;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

/**
 * Created by leo on 11/12/16.
 */
public class Login extends BaseController{

    public Login(Context ctx) {
        super(ctx);
    }

    public boolean realizaLogin(Usuario usuario, AbstractXMPPConnection conexao) throws IOException, SmackException, XMPPException {
        conexao.login(usuario.getLogin(), usuario.getSenha());
        UsuariosController usuariosController = new UsuariosController(ctx);
        //if (usuariosController.loginExiste()
        return true;
    }

    private IModel getTipoDeUsuario(Usuario usuario){

        return null;
    }
}
