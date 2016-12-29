package com.ifma.appmhelp.views;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.controls.ClientXMPPController;
import com.ifma.appmhelp.daos.IDao;
import com.ifma.appmhelp.daos.MedicosDao;
import com.ifma.appmhelp.daos.PacientesDao;
import com.ifma.appmhelp.daos.UsuariosDao;
import com.ifma.appmhelp.lib.BlowfishCrypt;
import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.Medico;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.Usuario;

import java.sql.SQLException;

public class CadastroActivity extends AppCompatActivity {

    private EditText edUsuarioCadastro;
    private EditText edSenhaCadastro;
    private RadioGroup rGroupCadastro;
    private EditText edNomeCadastro;
    private EditText edEmailCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        registrarComponentes();
    }

    public void cadastrar(View v){
        if(cadastroEhValido()){
            UsuariosDao usuariosDao = new UsuariosDao(this);
            ClientXMPPController clientXMPPController = new ClientXMPPController();
            try {
                Usuario novoUsuario = new Usuario(edUsuarioCadastro.getText().toString(), edSenhaCadastro.getText().toString());
                novoUsuario.setNome(edNomeCadastro.getText().toString());
                novoUsuario.setEmail(edEmailCadastro.getText().toString());

                //Se não existe usuário cadastrado no banco
                if(usuariosDao.getUsuarioByLogin(novoUsuario.getLogin()) == null) {
                    //Criptografa a senha antes de salvar no banco
                    novoUsuario.setSenha(BlowfishCrypt.encrypt(novoUsuario.getSenha()));
                    usuariosDao.persistir(novoUsuario, false);
                    this.registrarUsuario(novoUsuario);
                    Snackbar.make(findViewById(android.R.id.content), "Usuário cadastrado", Snackbar.LENGTH_LONG).show();

                    novoUsuario.setSenha(BlowfishCrypt.decrypt(novoUsuario.getSenha()));

                    if(!clientXMPPController.cadastrarUsuario(novoUsuario)){
                        novoUsuario.setSenha(BlowfishCrypt.encrypt(novoUsuario.getSenha()));
                        Snackbar.make(findViewById(android.R.id.content), "Não foi possível cadastrar, conexão não estabelecida", Snackbar.LENGTH_LONG).show();

                        usuariosDao.deletar(novoUsuario);
                    }

                }else
                    Snackbar.make(findViewById(android.R.id.content), "Usuário já existe", Snackbar.LENGTH_LONG).show();

                 } catch (Exception  e) {
                e.printStackTrace();
                Toast.makeText(this, "Erro ao cadastrar usuário - " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean registrarUsuario(Usuario usuario) throws SQLException {
        IDao controle;
        IModel modelo;
        if(rGroupCadastro.getCheckedRadioButtonId() == R.id.radioPaciente) {
            controle = new PacientesDao(this);
            modelo   = new Paciente(usuario);
        }else{
            controle = new MedicosDao(this);
            modelo   = new Medico(usuario);
        }
        controle.persistir(modelo, false);
        return true;
    }

    public void registrarComponentes(){
        edUsuarioCadastro = (EditText)   findViewById(R.id.edUsuarioCadastro);
        edSenhaCadastro   = (EditText)   findViewById(R.id.edSenhaCadastro);
        edEmailCadastro   = (EditText)   findViewById(R.id.edEmailCadastro);
        edNomeCadastro    = (EditText)   findViewById(R.id.edNomeCadastro);
        rGroupCadastro    = (RadioGroup) findViewById(R.id.rGroupCadastro);
    }

    private boolean cadastroEhValido(){
        if(edUsuarioCadastro.getText().toString().trim().equals("")){
            Snackbar.make(findViewById(android.R.id.content), "Preencha um usuário", Snackbar.LENGTH_LONG).show();
            edUsuarioCadastro.setFocusable(true);
            return false;
        }
        if(edSenhaCadastro.getText().toString().trim().equals("")){
            Snackbar.make(findViewById(android.R.id.content), "Preencha uma senha", Snackbar.LENGTH_LONG).show();
            edSenhaCadastro.setFocusable(true);
            return false;
        }
        if(rGroupCadastro.getCheckedRadioButtonId() < 0){
            Snackbar.make(findViewById(android.R.id.content), "Preencha um tipo de Usuário", Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;

    }



}
