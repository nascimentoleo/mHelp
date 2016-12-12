package com.ifma.appmhelp.controls;

import android.content.Context;
import android.content.SharedPreferences;

import com.ifma.appmhelp.lib.PasswordCrypt;
import com.ifma.appmhelp.models.ConexaoXMPP;
import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.Usuario;

/**
 * Created by leo on 11/12/16.
 */
public class Login extends BaseController{

    public Login(Context ctx) {
        super(ctx);
    }

    public IModel realizaLogin(Usuario usuario) throws Exception {
        if (loginEhValido(usuario)){
            ConexaoXMPP.getInstance().getConexao().login(usuario.getLogin(), usuario.getSenha());
            this.salvarArquivoDePreferencias(usuario,true);
            return carregaUsuario(usuario);
        }
        return null;
    }

    public void realizaLogoff() throws Exception {
        if(ConexaoXMPP.getInstance().conexaoFoiAutenticada()){
            ConexaoXMPP.getInstance().desconectar();
            this.salvarArquivoDePreferencias(new Usuario(), false);
        }
    }

    //Verifica se existe o usuário no banco, e se existir, compara as senhas oferecidas com a senha armazenada no banco
    //A senha fornecida no usuario deve estar sempre em texto pleno
    private boolean loginEhValido(Usuario usuario) throws Exception {
        UsuariosController usuariosController = new UsuariosController(ctx);
        Usuario usuarioDb =  usuariosController.getUsuarioByLogin(usuario.getLogin());

        if (usuarioDb != null) {
            String senha   = usuario.getSenha();
            String senhaDb = PasswordCrypt.decrypt(usuarioDb.getSenha());
            if (senha.equals(senhaDb))
                return true;
            else
                this.msgErro = "Senha incorreta";
        }else
            this.msgErro = "Usuário não existe";
        return false;
    }

    private IModel carregaUsuario(Usuario usuario) throws Exception {
        IModel result;
        usuario.setSenha(PasswordCrypt.encrypt(usuario.getSenha()));
        new UsuariosController(ctx).carregaId(usuario);
        result = new MedicosController(ctx).getMedicoByUsuario(usuario);
        if(result == null)
            result = new PacientesController(ctx).getPacienteByUsuario(usuario);
        return result;
    }

    private void salvarArquivoDePreferencias(Usuario usuario, boolean logado) throws Exception {
        SharedPreferences prefs = ctx.getSharedPreferences("preferences",0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("login", usuario.getLogin());
        editor.putString("senha", PasswordCrypt.encrypt(usuario.getSenha()));
        editor.putBoolean("logado", logado);
        editor.commit();
    }

    public Usuario getUsuarioLogado() throws Exception {
        SharedPreferences prefs = ctx.getSharedPreferences("preferences", 0);
        if (prefs.getBoolean("logado", false)){
            String login = prefs.getString("login", "");
            String senha = PasswordCrypt.decrypt(prefs.getString("senha", ""));
            return new Usuario(login,senha);
        }
        return null;
    }

}
