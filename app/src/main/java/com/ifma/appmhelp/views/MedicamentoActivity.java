package com.ifma.appmhelp.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.adapters.MedicamentosAdapter;
import com.ifma.appmhelp.daos.MedicamentoDao;
import com.ifma.appmhelp.enums.GenericBundleKeys;
import com.ifma.appmhelp.lib.EndlessRecyclerViewScrollListener;
import com.ifma.appmhelp.models.Medicamento;
import com.ifma.appmhelp.models.Paciente;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoActivity extends AppCompatActivity {

    private Paciente paciente;
    private RecyclerView rViewMedicamentos;
    private EditText edNomeMedicamento;
    private ArrayList<Medicamento> medicamentosDisponiveis;
    private int qtdRegistros = 20; //Quantidade de registros por refresh do adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicamento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.paciente = (Paciente) getIntent().getSerializableExtra(GenericBundleKeys.PACIENTE.toString());
        this.carregaComponentes();
        this.atualizaAdapter(0, qtdRegistros);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent returnIntent = new Intent();
                returnIntent.putExtra(GenericBundleKeys.PACIENTE.toString(),paciente);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
                return true;
        }
        return false;
    }

    private void carregaComponentes() {
        this.edNomeMedicamento = (EditText) findViewById(R.id.edMedicamentoNome);
        this.edNomeMedicamento.setOnEditorActionListener(new OnEditorActionListener());
        this.rViewMedicamentos = (RecyclerView) findViewById(R.id.rViewMedicamentos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        this.rViewMedicamentos.setLayoutManager(linearLayoutManager);
        this.rViewMedicamentos.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(totalItemsCount);

            }
        });

        this.medicamentosDisponiveis = new ArrayList<>();
        MedicamentosAdapter medicamentosAdapter = new MedicamentosAdapter(this, medicamentosDisponiveis);
        medicamentosAdapter.setOnItemLongClickListener(new AdicionarMedicamentoListener());
        rViewMedicamentos.setAdapter(medicamentosAdapter);

    }

    private void loadNextDataFromApi(int offset) {
        atualizaAdapter(offset, offset + qtdRegistros);
    }

    private void atualizaAdapter(int inicio, int fim) {
        try {
            List<Medicamento> listMedicamentos = null;
            MedicamentoDao dao = new MedicamentoDao(this);

            if(!edNomeMedicamento.getText().toString().isEmpty()) {
                listMedicamentos = dao.getMedicamentosByField(Long.valueOf(inicio), Long.valueOf(fim),
                        "nome", edNomeMedicamento.getText().toString().trim());
            }else
                listMedicamentos = dao.getMedicamentos(Long.valueOf(inicio),Long.valueOf(fim));

            medicamentosDisponiveis.addAll(listMedicamentos);
            //cidsDisponiveis.removeAll(cidsCadastrados);
            rViewMedicamentos.getAdapter().notifyDataSetChanged();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    class OnEditorActionListener implements EditText.OnEditorActionListener{

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_GO     || actionId == EditorInfo.IME_ACTION_NEXT ||
                    event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                medicamentosDisponiveis.clear();
                atualizaAdapter(0,qtdRegistros);
                return true;
            }
            return false;
        }
    }

    class AdicionarMedicamentoListener implements MedicamentosAdapter.OnItemLongClickListener{

        @Override
        public void onItemLongClick(Medicamento item) {

        }
    }


}
