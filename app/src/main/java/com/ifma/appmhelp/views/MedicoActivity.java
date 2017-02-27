package com.ifma.appmhelp.views;

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
import android.widget.Toast;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.controls.Login;
import com.ifma.appmhelp.daos.MedicoPacienteDao;
import com.ifma.appmhelp.daos.OcorrenciaDao;
import com.ifma.appmhelp.models.Medico;
import com.ifma.appmhelp.models.Ocorrencia;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.UsuarioLogado;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        medico = (Medico) UsuarioLogado.getInstance().getModelo();
        inicializaAdapter();
    }


    private void inicializaAdapter(){
        ArrayList<Ocorrencia> listaDeOcorrencias = (ArrayList<Ocorrencia>) this.carregaOcorrencias();

        if(listaDeOcorrencias != null)
            getSupportFragmentManager().beginTransaction().replace(R.id.container_list_ocorrencias_medico,ListOcorrenciasFragment.newInstance(listaDeOcorrencias)).commit();

    }

    private List<Ocorrencia> carregaOcorrencias() {
        List<Ocorrencia> ocorrencias = new ArrayList<>();
        try {
            List<Paciente> pacientes = new MedicoPacienteDao(this).getPacientesByMedico(this.medico);
            if (pacientes != null){
                OcorrenciaDao ocorrenciaDao = new OcorrenciaDao(this);
                for (Paciente paciente : pacientes){
                    ocorrencias.addAll(ocorrenciaDao.getOcorrenciasByPaciente(paciente));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao carregar ocorrências: " + e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        return ocorrencias;
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
        getMenuInflater().inflate(R.menu.medico, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch(item.getItemId()){
            case R.id.nav_alterar_dados :
                startActivity(new Intent(this, AlteraDadosActivity.class));
                break;

            case R.id.nav_adicionar_paciente :
                startActivity(new Intent(this, AdicionarPacienteActivity.class));
                break;

            case R.id.nav_pacientes :
                startActivity(new Intent(this,ListPacientesActivity.class));
                break;

            case R.id.nav_logoff_medico:
                try {
                    new Login(this).realizaLogoff();
                    Intent intent = new Intent(this, SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("msg_splash","Realizando logoff ...");
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Não foi possível realizar o logoff - "+ e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void OnOcorrenciaSelected(Ocorrencia ocorrencia) {
        this.abrirOcorrencia(ocorrencia);
    }

    private void abrirOcorrencia(Ocorrencia ocorrencia){

    }
}
