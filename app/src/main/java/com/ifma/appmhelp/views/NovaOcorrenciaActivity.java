package com.ifma.appmhelp.views;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.adapters.MedicosAdapter;
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
    private RecyclerView rViewMedicos;
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
        rViewMedicos = (RecyclerView) findViewById(R.id.rViewMedicosOcorrencia);
        rViewMedicos.setLayoutManager(new LinearLayoutManager(this));
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
            rViewMedicos.setAdapter(medicosAdapter);
        }
    }

    public void enviarOcorrencia(View v){
        if (ocorrenciaEhValida()){
            Ocorrencia ocorrencia = new Ocorrencia(edTitulo.getText().toString().trim(), this.paciente);
            Snackbar.make(findViewById(android.R.id.content), "Ocorrência enviada", Snackbar.LENGTH_LONG).show();
            try {
                new OcorrenciaDao(this).persistir(ocorrencia, false);
            } catch (SQLException e) {
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
        if (!medicosAdapter.existemMedicosSelecionados()){
            Snackbar.make(findViewById(android.R.id.content), "Não existem médicos selecionados", Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}
