package com.ifma.appmhelp.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.controls.Login;
import com.ifma.appmhelp.models.Medico;
import com.ifma.appmhelp.models.Ocorrencia;
import com.ifma.appmhelp.models.UsuarioLogado;

public class MedicoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ListOcorrenciasFragment.OnOcorrenciaSelectedListener  {

    private Medico medico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medico);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        medico = (Medico) UsuarioLogado.getInstance().getModelo();
        inicializaAdapter();
    }

    private void inicializaAdapter(){
        getSupportFragmentManager().beginTransaction().replace(R.id.container_list_ocorrencias_medico,ListOcorrenciasFragment.newInstance(true)).commit();

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
        getMenuInflater().inflate(R.menu.medico, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.nav_alterar_dados : this.alterarDados(); break;

            case R.id.nav_adicionar_paciente : this.adicionarPaciente(); break;

            case R.id.nav_pacientes : this.exibePacientes(); break;

            case R.id.nav_logoff_medico: this.logoff();  break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void alterarDados(){
        startActivity(new Intent(this, AlteraDadosActivity.class));
    }

    private void exibePacientes(){
        startActivity(new Intent(this,ListPacientesActivity.class));
    }

    private void adicionarPaciente(){
        startActivity(new Intent(this, AdicionarPacienteActivity.class));
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
        this.abrirOcorrencia(ocorrencia);
    }

    private void abrirOcorrencia(Ocorrencia ocorrencia){

    }
}
