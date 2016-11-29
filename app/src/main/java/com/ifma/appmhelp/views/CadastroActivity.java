package com.ifma.appmhelp.views;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.controls.RepositorioDeUsuarios;
import com.ifma.appmhelp.models.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private EditText edUsuarioCadastro;
    private EditText edSenhaCadastro;
    private RadioGroup rGroupCadastro;
    private EditText edCRM;
    private TextInputLayout txtCRM;

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
            RepositorioDeUsuarios cadastro = new RepositorioDeUsuarios();
            if (cadastro.cadastrar(this, novoUsuario))
                Toast.makeText(this, "Usuário cadastrado", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Erro ao cadastrar usuário - " + cadastro.getMsgErro(), Toast.LENGTH_SHORT).show();

        }
    }

    public void onClickRadioGroupCadastro(View v){
        if (rGroupCadastro.getCheckedRadioButtonId() == R.id.radioMedico)
            this.txtCRM.setVisibility(View.VISIBLE);
        else
            this.txtCRM.setVisibility(View.INVISIBLE);
    }

    public void registrarComponentes(){
        edUsuarioCadastro = (EditText) findViewById(R.id.edUsuarioCadastro);
        edSenhaCadastro   = (EditText) findViewById(R.id.edSenhaCadastro);
        edCRM             = (EditText) findViewById(R.id.edCRM);
        rGroupCadastro    = (RadioGroup) findViewById(R.id.rGroupCadastro);
        txtCRM            = (TextInputLayout) findViewById(R.id.txtCRM);
        txtCRM.setVisibility(View.INVISIBLE);
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
        return true;

    }



}
