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
import com.ifma.appmhelp.controls.CadastroDeMedicos;
import com.ifma.appmhelp.controls.CadastroDePacientes;
import com.ifma.appmhelp.controls.CadastroDeUsuarios;
import com.ifma.appmhelp.models.Medico;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.Usuario;

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
            Usuario novoUsuario = new Usuario(edUsuarioCadastro.getText().toString(), edSenhaCadastro.getText().toString());
            novoUsuario.setNome(edNomeCadastro.getText().toString());
            novoUsuario.setEmail(edEmailCadastro.getText().toString());
            CadastroDeUsuarios cadastroDeUsuarios = new CadastroDeUsuarios();
            if (cadastroDeUsuarios.cadastrar(this, novoUsuario)) {
                if(registrarUsuario(novoUsuario))
                    Toast.makeText(this, "Usuário cadastrado", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "Erro ao cadastrar usuário - " + cadastroDeUsuarios.getMsgErro(), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean registrarUsuario(Usuario usuario){
        //Paciente
        if(rGroupCadastro.getCheckedRadioButtonId() == R.id.radioPaciente){
            CadastroDePacientes cadastroDePacientes = new CadastroDePacientes();
            if (!cadastroDePacientes.persistir(this,new Paciente(usuario))){
                Toast.makeText(this, "Paciente não cadastrado - " + cadastroDePacientes.getMsgErro(), Toast.LENGTH_SHORT).show();
                return false;
            }

        }else{
            CadastroDeMedicos cadastroDeMedicos = new CadastroDeMedicos();
            if(!cadastroDeMedicos.persistir(this, new Medico(usuario))) {
                Toast.makeText(this, "Médico não cadastrado - " + cadastroDeMedicos.getMsgErro(), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
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
