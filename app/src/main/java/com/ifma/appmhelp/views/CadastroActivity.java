package com.ifma.appmhelp.views;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.controls.ClientXMPPController;
import com.ifma.appmhelp.controls.IController;
import com.ifma.appmhelp.controls.MedicosController;
import com.ifma.appmhelp.controls.PacientesController;
import com.ifma.appmhelp.controls.UsuariosController;
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        registrarComponentes();
    }

    public void cadastrar(View v){
        if(cadastroEhValido()){
            UsuariosController controleDeUsuarios = new UsuariosController(this);
            ClientXMPPController clientXMPPController = new ClientXMPPController();
            try {
                Usuario novoUsuario = new Usuario(edUsuarioCadastro.getText().toString(), edSenhaCadastro.getText().toString());
                novoUsuario.setNome(edNomeCadastro.getText().toString());
                novoUsuario.setEmail(edEmailCadastro.getText().toString());

                if(clientXMPPController.cadastrarUsuario(novoUsuario)){
                    //Se não existe usuário cadastrado no banco
                    if(controleDeUsuarios.getUsuarioByLogin(novoUsuario.getLogin()) == null) {
                        controleDeUsuarios.persistir(novoUsuario);
                        this.registrarUsuario(novoUsuario);
                        Toast.makeText(this, "Usuário cadastrado", Toast.LENGTH_SHORT).show();

                    }else
                        Toast.makeText(this,"Usuário já existe",Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(this,"Não foi possível cadastrar, conexão não estabelecida",Toast.LENGTH_SHORT).show();
            } catch (Exception  e) {
                e.printStackTrace();
                Toast.makeText(this, "Erro ao cadastrar usuário - " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean registrarUsuario(Usuario usuario) throws SQLException {
        IController controle;
        IModel modelo;
        if(rGroupCadastro.getCheckedRadioButtonId() == R.id.radioPaciente) {
            controle = new PacientesController(this);
            modelo   = new Paciente(usuario);
        }else{
            controle = new MedicosController(this);
            modelo   = new Medico(usuario);
        }
        controle.persistir(modelo);
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
            Toast.makeText(this, "Preencha um usuário", Toast.LENGTH_SHORT).show();
            edUsuarioCadastro.setFocusable(true);
            return false;
        }
        if(edSenhaCadastro.getText().toString().trim().equals("")){
            Toast.makeText(this, "Preencha uma senha", Toast.LENGTH_SHORT).show();
            edSenhaCadastro.setFocusable(true);
            return false;
        }
        if(rGroupCadastro.getCheckedRadioButtonId() < 0){
            Toast.makeText(this, "Preencha um tipo de Usuário", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }



}
