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
import com.ifma.appmhelp.controls.CadastroController;
import com.ifma.appmhelp.enums.TipoUsuario;
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        registrarComponentes();
    }

    public void cadastrar(View v){
        if(cadastroEhValido()){
            Usuario novoUsuario = new Usuario(edUsuarioCadastro.getText().toString(), edSenhaCadastro.getText().toString());
            novoUsuario.setNome(edNomeCadastro.getText().toString());
            novoUsuario.setEmail(edEmailCadastro.getText().toString());

            TipoUsuario tipoUsuario;
            if(rGroupCadastro.getCheckedRadioButtonId() == R.id.radioPaciente)
                tipoUsuario = TipoUsuario.PACIENTE;
            else
                tipoUsuario = TipoUsuario.MEDICO;

            CadastroController cadastroController = new CadastroController(this);

            if (cadastroController.cadastrar(novoUsuario, tipoUsuario))
                Snackbar.make(findViewById(android.R.id.content), "Usuário cadastrado", Snackbar.LENGTH_LONG).show();
            else
                Toast.makeText(this,cadastroController.getMsgErro(),Toast.LENGTH_SHORT).show();
        }
    }

    public void registrarComponentes(){
        edUsuarioCadastro = (EditText)   findViewById(R.id.edUsuarioCadastro);
        edSenhaCadastro   = (EditText)   findViewById(R.id.edSenhaCadastro);
        edEmailCadastro   = (EditText)   findViewById(R.id.edEmailCadastro);
        edNomeCadastro    = (EditText)   findViewById(R.id.edNomeCadastro);
        rGroupCadastro    = (RadioGroup) findViewById(R.id.rGroupCadastro);
    }

    private boolean cadastroEhValido(){

        if(edNomeCadastro.getText().toString().trim().equals("")){
            Snackbar.make(findViewById(android.R.id.content), "Preencha um nome", Snackbar.LENGTH_LONG).show();
            edNomeCadastro.setFocusable(true);
            return false;
        }

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
