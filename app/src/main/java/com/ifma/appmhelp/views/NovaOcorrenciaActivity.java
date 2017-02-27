package com.ifma.appmhelp.views;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.adapters.MedicosAdapter;
import com.ifma.appmhelp.controls.OcorrenciasController;
import com.ifma.appmhelp.daos.MedicoPacienteDao;
import com.ifma.appmhelp.daos.OcorrenciaDao;
import com.ifma.appmhelp.models.Medico;
import com.ifma.appmhelp.models.Ocorrencia;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.UsuarioLogado;

import java.sql.SQLException;
import java.util.List;

public class NovaOcorrenciaActivity extends AppCompatActivity {

    private Paciente paciente;
    private Spinner spMedicos;
    private EditText edTitulo;
    private MedicosAdapter medicosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_ocorrencia);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inicializaComponentes();
        paciente = (Paciente) UsuarioLogado.getInstance().getModelo();
        inicializaAdapter();
    }


    private void inicializaComponentes(){
        edTitulo     = (EditText) findViewById(R.id.edTituloOcorrencia);
        spMedicos    = (Spinner) findViewById(R.id.spMedicos);
    }

    private List<Medico> carregaMedicos() {
        try {
            return new MedicoPacienteDao(this).getMedicosByPaciente(this.paciente);

        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao carregar ocorrências: " + e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    private void inicializaAdapter(){
        List<Medico> listaDeMedicos = this.carregaMedicos();

        if(listaDeMedicos != null){
            medicosAdapter = new MedicosAdapter(this,listaDeMedicos);
            spMedicos.setAdapter(medicosAdapter);
        }else{
            Toast.makeText(this, "Não é possível gerar ocorrências, pois não existem médicos adicionados",Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    public void enviarOcorrencia(View v){
        if (ocorrenciaEhValida()){
            String titulo = edTitulo.getText().toString().trim();
            Medico medico = medicosAdapter.getItem(spMedicos.getSelectedItemPosition());

            try {
                Ocorrencia ocorrencia = new Ocorrencia(titulo, this.paciente.clone(), medico.clone());
                new OcorrenciaDao(this).persistir(ocorrencia, false);
                new OcorrenciasController(this).enviarNovaOcorrencia(medico.getUsuario(), ocorrencia);
                Snackbar.make(findViewById(android.R.id.content), "Ocorrência enviada", Snackbar.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Erro ao cadastrar ocorrência - " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean ocorrenciaEhValida(){
        if(edTitulo.getText().toString().trim().equals("")){
            Snackbar.make(findViewById(android.R.id.content), "Preencha um título", Snackbar.LENGTH_LONG).show();
            edTitulo.setFocusable(true);
            return false;
        }


        return true;
    }

}
