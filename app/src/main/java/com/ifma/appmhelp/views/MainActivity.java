package com.ifma.appmhelp.views;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
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
import com.ifma.appmhelp.tasks.ConexaoXMPPService;

public class MainActivity extends AppCompatActivity
        implements ServiceConnection, NavigationView.OnNavigationItemSelectedListener {

    private ProgressDialog load;
    private EditText edLogin;
    private EditText edSenha;
    private Login login;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getBooleanExtra("finalizou_conexao", false))
                Toast.makeText(getApplicationContext(), "Conectou", Toast.LENGTH_SHORT).show();
        }
    };

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
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("conectar"));
        //this.conectar();
    }

    @Override
    protected void onStart() {
        this.conectar();
        /*if(ConexaoXMPP.getInstance().conexaoFoiAutenticada())
           ConexaoXMPP.getInstance().desconectar();
        this.conectar(); */

        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*Intent it = new Intent(this, ConexaoXMPPService.class);
        startService(it);
        bindService(it, this, 0); */
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unbindService(this);

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
            case R.id.nav_cadastrar :  if(ConexaoXMPP.getInstance().conexaoEstaAtiva())
                                         startActivity(new Intent(this, CadastroActivity.class));
                                       else
                                         Toast.makeText(this, "Não é possível realizar cadastro, " +
                                                              "pois não foi feita a conexão com o servidor", Toast.LENGTH_SHORT).show();
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
        new ConectarXMPPTask(this.load).execute(new Host("192.168.1.24", 5222)); //Ip para device
        /*if(!ConexaoXMPP.getInstance().conexaoEstaAtiva()) {
            //this.conectarXMPPTask.execute(new Host("10.0.2.2", 5222)); //Ip para avd
            //new ConectarXMPPTask(this.load).execute(new Host("192.168.1.24", 5222)); //Ip para device
            new ConectarXMPPTask(this.load).execute(new Host("192.168.0.6", 5222)); //Ip para device

            /*Usuario usuarioLogado = login.getUsuarioLogado();
            if(usuarioLogado != null)
                this.logar(usuarioLogado); */
        //}
    }

    public void efetuarLogin(View v){
        if(ConexaoXMPP.getInstance().conexaoEstaAtiva()){
            if(loginEhValido())
                try {
                    this.logar(new Usuario(edLogin.getText().toString(), edSenha.getText().toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
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
        } catch (Exception e) {
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

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        ConexaoXMPPService.ConexaoXMPPBinder binder = (ConexaoXMPPService.ConexaoXMPPBinder) service;
        ConexaoXMPP.getInstance().setConexao(binder.getConexao());
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
       ConexaoXMPP.getInstance().setConexao(null);
    }
}
