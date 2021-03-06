package com.ifma.appmhelp.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.controls.Login;
import com.ifma.appmhelp.factories.FactoryLogadoActivity;
import com.ifma.appmhelp.lib.KeyboardLib;
import com.ifma.appmhelp.models.ConexaoXMPP;
import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.Usuario;
import com.ifma.appmhelp.models.UsuarioLogado;

public class LoginActivity extends AppCompatActivity
        implements  NavigationView.OnNavigationItemSelectedListener {

    private EditText edLogin;
    private EditText edSenha;
    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        this.registrarComponentes();

    }

    @Override
    protected void onStart() {
        super.onStart();
        this.relogarUsuarioSalvo();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch(item.getItemId()){
            case R.id.nav_cadastrar :  if(ConexaoXMPP.getInstance().conexaoEstaAtiva())
                                         startActivity(new Intent(this, CadastroActivity.class));
                                       else
                Snackbar.make(findViewById(android.R.id.content), "Não é possível realizar cadastro, " +
                        "pois não foi feita a conexão com o servidor", Snackbar.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void registrarComponentes(){
        this.edLogin = (EditText) findViewById(R.id.edUsuarioLogin);
        this.edSenha = (EditText) findViewById(R.id.edUsuarioSenha);
        this.login = new Login(this);
    }

    public void efetuarLogin(View v){
        KeyboardLib.fecharTeclado(this);
        if(ConexaoXMPP.getInstance().conexaoEstaAtiva()){
            if(loginEhValido())
                try {
                    this.logar(new Usuario(edLogin.getText().toString().trim(), edSenha.getText().toString().trim()));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
        }else
            Snackbar.make(findViewById(android.R.id.content), "Não é possível fazer o login, " +
                    "pois não foi feita a conexão com o servidor", Snackbar.LENGTH_LONG).show();  }

    private void relogarUsuarioSalvo(){
        if(ConexaoXMPP.getInstance().conexaoEstaAtiva()){
            try {
                Usuario usuarioLogado = new Login(this).getUsuarioLogado();
                if(usuarioLogado != null)
                    this.logar(usuarioLogado);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Erro ao relogar - " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void logar(Usuario usuario) throws Exception {
        try {
            IModel usuarioLogado = login.realizaLogin(usuario);
            if(usuarioLogado != null){
                UsuarioLogado.getInstance().setModelo(usuarioLogado);
                Class activityClass = FactoryLogadoActivity.getActivityClass(usuarioLogado);
                startActivity(new Intent(this, activityClass));
            }else
                Toast.makeText(this, login.getMsgErro(),
                        Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            new Login(this).realizaLogoff();
        }
    }

    private boolean loginEhValido() {
        if (edLogin.getText().toString().trim().equals("")) {
            Snackbar.make(findViewById(android.R.id.content), "Preencha um usuário", Snackbar.LENGTH_LONG).show();
            edLogin.setFocusable(true);
            return false;
        }
        else if (edSenha.getText().toString().trim().equals("")) {
            Snackbar.make(findViewById(android.R.id.content), "Preencha uma senha", Snackbar.LENGTH_LONG).show();
            edSenha.setFocusable(true);
            return false;
        }
        return true;
    }


}
