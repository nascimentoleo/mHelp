package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.models.ConexaoXMPP;
import com.ifma.appmhelp.models.Usuario;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.iqregister.AccountManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by leo on 11/28/16.
 */
public class RepositorioDeUsuarios {

    private String msgErro;

    public String getMsgErro() {
        return msgErro;
    }

    public boolean cadastrar(Context ctx, Usuario usuario){
        DbSqlHelper databaseHelper = OpenHelperManager.getHelper(ctx, DbSqlHelper.class);
        try {
            Dao<Usuario,Long> usuarioDao = databaseHelper.getDao(Usuario.class);
            if(this.addUsuarioNoServidorXMPP(usuario)){
                if(getUsuarioByLogin(usuario.getLogin(), usuarioDao) == null) {
                    usuarioDao.create(usuario);
                    return true;
                }else
                     this.msgErro = "Usuário já existe no banco";
            }
        } catch (SQLException e) { e.printStackTrace();
            this.msgErro = e.getMessage();
        } finally{
            databaseHelper.close();
        }
        return false;
    }


    private Usuario getUsuarioByLogin(String login,Dao<Usuario,Long> usuarioDao) throws SQLException {
        List<Usuario> listaDeUsuarios =  usuarioDao.queryForEq("login", login);
        if (listaDeUsuarios.size() > 0)
            return listaDeUsuarios.get(0);
        return null;
    }

    private boolean addUsuarioNoServidorXMPP(Usuario usuario){
        try {
            if(ConexaoXMPP.getInstance().conexaoEstaAtiva()){
                AccountManager accountManager = AccountManager.getInstance(ConexaoXMPP.getInstance().getConexao());
                accountManager.createAccount(usuario.getLogin(), usuario.getSenha());
                return true;
            }
        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException |SmackException.NotConnectedException e ) {
            e.printStackTrace();
            this.msgErro = e.getMessage();
        }
        return false;
    }
}
