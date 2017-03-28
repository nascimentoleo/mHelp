package com.ifma.appmhelp.views;

import android.content.Intent;
import android.net.Uri;
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
import com.ifma.appmhelp.controls.OcorrenciaPagination;
import com.ifma.appmhelp.enums.GenericBundleKeys;
import com.ifma.appmhelp.models.Ocorrencia;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.UsuarioLogado;
import com.ifma.appmhelp.services.FileTransfer;

public class PacienteActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ListOcorrenciasFragment.OnOcorrenciaSelectedListener {

    private Paciente paciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente);
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

        paciente = (Paciente) UsuarioLogado.getInstance().getModelo();
        this.inicializaAdapter();
    }

    private void inicializaAdapter(){
        OcorrenciaPagination ocorrenciaPagination = new OcorrenciaPagination(this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.paciente, menu);
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
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), 1234);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent intent;
        switch(item.getItemId()){
            case R.id.nav_alterar_dados :
                  startActivity(new Intent(this, AlteraDadosActivity.class));
                break;
            case R.id.nav_adicionar_medico :
                intent = new Intent(this, AdicionarMedicoActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_prontuario :
                if (paciente.getProntuario() != null){
                    intent = new Intent(this, ProntuarioActivity.class);
                    intent.putExtra(GenericBundleKeys.PRONTUARIO.toString(), paciente.getProntuario());
                    startActivity(intent);
                }else
                    Toast.makeText(this, "Paciente não possui prontuário cadastrado ", Toast.LENGTH_SHORT).show();


                break;
            case R.id.nav_nova_ocorrencia :
                intent = new Intent(this, NovaOcorrenciaActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_logoff_paciente:
                try {
                    new Login(this).realizaLogoff();
                    intent = new Intent(this, SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("msg_splash","Realizando logoff ...");
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Não foi possível realizar o logoff - " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1234 && resultCode == RESULT_OK) {
            //imagem veio da galeria
            Uri uriImagemGaleria = data.getData();
            FileTransfer.uploadFile(this, uriImagemGaleria);
        }
    }
}
