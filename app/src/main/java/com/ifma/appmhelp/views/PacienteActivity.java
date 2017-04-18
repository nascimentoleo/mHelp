package com.ifma.appmhelp.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.controls.Login;
import com.ifma.appmhelp.enums.GenericBundleKeys;
import com.ifma.appmhelp.models.Ocorrencia;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.UsuarioLogado;

public class PacienteActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ListOcorrenciasFragment.OnOcorrenciaSelectedListener {

    private Paciente paciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        paciente = (Paciente) UsuarioLogado.getInstance().getModelo();
        toolbar.setTitle(paciente.getUsuario().getNome());

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.inicializaAdapter();
        this.carregaComponentes();
    }

    private void carregaComponentes() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               novaOcorrencia();
            }
        });
    }

    private void inicializaAdapter(){
        getSupportFragmentManager().beginTransaction().replace(R.id.container_list_ocorrencias_paciente,ListOcorrenciasFragment.newInstance(false)).commit();

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

        switch(item.getItemId()){
            case R.id.nav_alterar_dados : this.alterarDados(); break;

            case R.id.nav_adicionar_medico : this.adicionarMedico(); break;

            case R.id.nav_prontuario : this.abrirProntuario(); break;

            case R.id.nav_nova_ocorrencia : this.novaOcorrencia();break;

            case R.id.nav_logoff_paciente: this.logoff(); break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void novaOcorrencia(){
        startActivity(new Intent(this, NovaOcorrenciaActivity.class));
    }

    private void adicionarMedico(){
        startActivity(new Intent(this, AdicionarMedicoActivity.class));
    }

    private void alterarDados(){
        startActivity(new Intent(this, AlteraDadosActivity.class));
    }

    private void abrirProntuario(){
        if (paciente.getProntuario() != null){
            Intent intent = new Intent(this, ProntuarioActivity.class);
            intent.putExtra(GenericBundleKeys.PRONTUARIO.toString(), paciente.getProntuario());
            startActivity(intent);
        }else
            Toast.makeText(this, "Paciente não possui prontuário cadastrado ", Toast.LENGTH_SHORT).show();
    }

    private void logoff(){
        try {
            new Login(this).realizaLogoff();
            Intent intent = new Intent(this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("msg_splash","Realizando logoff ...");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Não foi possível realizar o logoff - " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnOcorrenciaSelected(Ocorrencia ocorrencia) {

    }
}
