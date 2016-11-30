package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.models.ConexaoXMPP;
import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.Usuario;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by leo on 11/12/16.
 */
public class Login extends BaseController{

    public Login(Context ctx) {
        super(ctx);
    }

    public IModel realizaLogin(Usuario usuario) throws SQLException, XMPPException, SmackException, IOException {
        if (loginEhValido(usuario)){
            ConexaoXMPP.getInstance().getConexao().login(usuario.getLogin(), usuario.getSenha());
            return carregaUsuario(usuario);
        }
        return null;
    }

    private boolean loginEhValido(Usuario usuario) throws SQLException {
        UsuariosController usuariosController = new UsuariosController(ctx);
        Usuario usuarioDb =  usuariosController.getUsuarioByLogin(usuario.getLogin());
        if (usuarioDb != null)
            if(usuario.getSenha().equals(usuarioDb.getSenha()))
                return true;
            else
                this.msgErro = "Senha incorreta";
        else
            this.msgErro = "Usuário não existe";
        return false;
    }

    private IModel carregaUsuario(Usuario usuario) throws SQLException {
        IModel result;
        IController controller = new MedicosController(ctx);
        result = ((MedicosController) controller).getMedicoByUsuario(usuario);
        if(result == null) {
            controller = new PacientesController(ctx);
            result = ((PacientesController) controller).getPacienteByUsuario(usuario);
        }
        return result;
    }

}
