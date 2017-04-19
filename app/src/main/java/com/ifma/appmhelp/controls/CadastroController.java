package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.daos.IDao;
import com.ifma.appmhelp.daos.MedicoDao;
import com.ifma.appmhelp.daos.PacienteDao;
import com.ifma.appmhelp.daos.UsuarioDao;
import com.ifma.appmhelp.enums.TipoUsuario;
import com.ifma.appmhelp.lib.BlowfishCrypt;
import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.Medico;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.Usuario;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.sql.SQLException;

/**
 * Created by leo on 4/19/17.
 */

public class CadastroController extends BaseController {

    public CadastroController(Context ctx) {
        super(ctx);
    }

    public boolean cadastrar(Usuario usuario, TipoUsuario tipoUsuario){
        //Cadastra no smack
        if (this.cadastrarUsuarioXMPP(usuario)){
            UsuarioDao usuarioDao = new UsuarioDao(ctx);
            try {
                if(usuarioDao.getUsuarioByLogin(usuario.getLogin()) == null) {

                    usuario.setSenha(BlowfishCrypt.encrypt(usuario.getSenha()));
                    usuarioDao.persistir(usuario, false);
                    this.registrarUsuario(usuario, tipoUsuario);

                    return true;

                }else
                    msgErro = "Usuário já cadastrado!";
            } catch (Exception e) {
                e.printStackTrace();
                msgErro = e.getMessage();
            }
        }

        return false;
    }


    private boolean cadastrarUsuarioXMPP(Usuario usuario){
        try {
            if(ClientXMPPController.cadastrarUsuario(usuario))
                return true;
            else
                this.msgErro = "Conexão ao servidor XMPP não está ativa";

        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
            msgErro = "Servidor XMPP está offline";

        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
            msgErro = "Conexão ao servidor XMPP não está ativa";

        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
            msgErro = "Usuário já existe no servidor XMPP";

        }

        return false;
    }

    private void registrarUsuario(Usuario usuario, TipoUsuario tipoUsuario) throws SQLException {

        IDao controle;
        IModel modelo;

        if(tipoUsuario == TipoUsuario.PACIENTE) {
            controle = new PacienteDao(ctx);
            modelo   = new Paciente(usuario);
        }else{
            controle = new MedicoDao(ctx);
            modelo   = new Medico(usuario);
        }

        controle.persistir(modelo, false);
    }
}
