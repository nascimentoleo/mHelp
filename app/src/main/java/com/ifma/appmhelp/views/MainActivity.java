package com.ifma.appmhelp.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.controls.Login;
import com.ifma.appmhelp.enums.BundleKeys;
import com.ifma.appmhelp.factories.FactoryLogadoActivity;
import com.ifma.appmhelp.models.ConexaoXMPP;
import com.ifma.appmhelp.models.Host;
import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.Usuario;
import com.ifma.appmhelp.tasks.ConectarXMPPTask;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ProgressDialog load;
    private EditText edLogin;
    private EditText edSenha;
    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        if(ConexaoXMPP.getInstance().conexaoFoiAutenticada())
           ConexaoXMPP.getInstance().desconectar();
        this.conectar();

        super.onStart();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch(item.getItemId()){
            case R.id.nav_cadastrar :  startActivity(new Intent(this, CadastroActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void registrarComponentes(){
        this.load  = new ProgressDialog(this);
        this.edLogin = (EditText) findViewById(R.id.edUsuarioLogin);
        this.edSenha = (EditText) findViewById(R.id.edUsuarioSenha);
        this.login = new Login(this);
    }

    public void conectar() {
        if(!ConexaoXMPP.getInstance().conexaoEstaAtiva()) {
            //this.conectarXMPPTask.execute(new Host("10.0.2.2", 5222)); //Ip para avd
            //new ConectarXMPPTask(this.load).execute(new Host("192.168.1.24", 5222)); //Ip para device
            new ConectarXMPPTask(this.load).execute(new Host("192.168.0.6", 5222)); //Ip para device

            /*Usuario usuarioLogado = login.getUsuarioLogado();
            if(usuarioLogado != null)
                this.logar(usuarioLogado); */
        }
    }

    public void efetuarLogin(View v){
        if(ConexaoXMPP.getInstance().conexaoEstaAtiva()){
            if(loginEhValido())
                this.logar(new Usuario(edLogin.getText().toString(), edSenha.getText().toString()));
        }else
             Toast.makeText(this, "Não foi possível fazer login, pois não foi feita a conexão com o servidor", Toast.LENGTH_SHORT).show();
    }

    private void logar(Usuario usuario){
        try {
            IModel usuarioLogado = login.realizaLogin(usuario);
            if(usuarioLogado != null){
                Toast.makeText(this, "Bem vindo " + usuario.getLogin(),
                        Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putSerializable(BundleKeys.USUARIO_LOGADO.getValue(), usuarioLogado);
                Class activityClass = FactoryLogadoActivity.getActivityClass(usuarioLogado);
                startActivity(new Intent(this, activityClass).putExtras(bundle));
            }else
                Toast.makeText(this, login.getMsgErro(),
                        Toast.LENGTH_SHORT).show();
        } catch (IOException |SmackException |XMPPException | SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private boolean loginEhValido() {
        if (edLogin.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Preencha um usuário", Toast.LENGTH_SHORT).show();
            edLogin.setFocusable(true);
            return false;
        }
        else if (edSenha.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Preencha uma senha", Toast.LENGTH_SHORT).show();
            edSenha.setFocusable(true);
            return false;
        } else
            return true;
    }

}
