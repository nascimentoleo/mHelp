package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.models.ConexaoXMPP;
import com.ifma.appmhelp.models.Host;
import com.ifma.appmhelp.models.Usuario;
import com.ifma.appmhelp.services.ConectarXMPPTask;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.iqregister.AccountManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by leo on 11/30/16.
 */
public class ClientXMPPController{

    public boolean cadastrarUsuario(Usuario usuario) throws Exception{
        if (ConexaoXMPP.getInstance().conexaoEstaAtiva()){
            AccountManager accountManager = AccountManager.getInstance(ConexaoXMPP.getInstance().getConexao());
            Map<String, String> atributes = new HashMap<>();
            atributes.put("name", usuario.getNome());
            atributes.put("email", usuario.getEmail());
            accountManager.createAccount(usuario.getLogin(), usuario.getSenha(), atributes);
            return true;
        }
        return false;

    }

    public static ConectarXMPPTask conectar(Context ctx){
        ConectarXMPPTask conectarTask = new ConectarXMPPTask(ctx);
        conectarTask.execute(new Host());
        return conectarTask;
    }

    public static void autenticar(Usuario usuario) throws IOException, XMPPException, SmackException {
        if (usuario != null)
            ConexaoXMPP.getInstance().getConexao().login(usuario.getLogin(), usuario.getSenha());
    }

}
