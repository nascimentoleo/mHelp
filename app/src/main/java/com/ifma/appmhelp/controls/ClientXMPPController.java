package com.ifma.appmhelp.controls;

import com.ifma.appmhelp.models.ConexaoXMPP;
import com.ifma.appmhelp.models.Usuario;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.iqregister.AccountManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leo on 11/30/16.
 */
public class ClientXMPPController{

    public boolean cadastrarUsuario(Usuario usuario) throws SmackException.NoResponseException, XMPPException.XMPPErrorException, SmackException.NotConnectedException {
        if (ConexaoXMPP.getInstance().conexaoEstaAtiva()){
            AccountManager accountManager = AccountManager.getInstance(ConexaoXMPP.getInstance().getConexao());
            Map<String, String> atributes = new HashMap<>();
            atributes.put("name", usuario.getNome());
            atributes.put("email", usuario.getEmail());
            accountManager.createAccount(usuario.getLogin(), usuario.getSenha(),atributes);
            return true;
        }
        return false;

    }
}
