package com.ifma.appmhelp.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.adapters.RecycleCidsAdapter;
import com.ifma.appmhelp.daos.CidDao;
import com.ifma.appmhelp.enums.GenericBundleKeys;
import com.ifma.appmhelp.lib.EndlessRecyclerViewScrollListener;
import com.ifma.appmhelp.models.Cid;
import com.ifma.appmhelp.models.Paciente;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CidActivity extends AppCompatActivity {

    private Paciente paciente;
    private RecyclerView rViewCids;
    private EditText edCidCodigo;
    private EditText edCidDescricao;
    private int qtdRegistros = 20; //Quantidade de registros por refresh do adapter
    private ArrayList<Cid> adapterCid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cid);
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

    private void carregaComponentes(){
        this.edCidCodigo = (EditText) findViewById(R.id.edCidCodigo);
        this.edCidDescricao = (EditText) findViewById(R.id.edCidDescricao);
        this.rViewCids = (RecyclerView) findViewById(R.id.rViewCids);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        this.rViewCids.setLayoutManager(linearLayoutManager);
        this.rViewCids.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(totalItemsCount);

            }
        });

        this.adapterCid = new ArrayList<>();
        this.rViewCids.setAdapter(new RecycleCidsAdapter(this,adapterCid));
    }

    private void atualizaAdapter(int inicio, int fim){
        try {
            List<Cid> listCids = new CidDao(this).getCids(Long.valueOf(inicio),Long.valueOf(fim));
            adapterCid.addAll(listCids);
            rViewCids.getAdapter().notifyDataSetChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Cid> carregaCids(int qtdRegistros){
        try {
            return new CidDao(this).getTodos(qtdRegistros);
        } catch (SQLException e) {
            Toast.makeText(this, "Não foi possível carregar os cids: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return null;
        }
    }

    public void loadNextDataFromApi(int offset) {
        try {
            List<Cid> listCids = new CidDao(this).getCids(Long.valueOf(offset),Long.valueOf(offset + qtdRegistros));
            adapterCid.addAll(listCids);
            rViewCids.getAdapter().notifyDataSetChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
