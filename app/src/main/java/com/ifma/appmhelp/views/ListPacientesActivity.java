package com.ifma.appmhelp.views;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.daos.MedicoPacienteDao;
import com.ifma.appmhelp.models.Medico;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.UsuarioLogado;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListPacientesActivity extends AppCompatActivity implements ListPacientesFragment.OnPatientSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pacientes);
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
        ArrayList<Paciente> listaDePacientes = (ArrayList<Paciente>) this.carregaPacientes();
        if(listaDePacientes != null){
            getSupportFragmentManager().beginTransaction().add(R.id.container_list_pacientes,ListPacientesFragment.newInstance(listaDePacientes)).commit();
        }else {
            Toast.makeText(this, "Este médico não possui pacientes adicionados", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private List<Paciente> carregaPacientes(){
        Medico medico = (Medico) UsuarioLogado.getInstance().getModelo();
        try {
            return new MedicoPacienteDao(this).getPacientesByMedico(medico);
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao carregar pacientes: " + e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    public void onPacienteSelected(Paciente paciente) {
        Paciente novo = paciente;
    }
}
