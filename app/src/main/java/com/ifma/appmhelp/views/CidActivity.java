package com.ifma.appmhelp.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.Toast;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.adapters.CidsAdapter;
import com.ifma.appmhelp.daos.CidDao;
import com.ifma.appmhelp.enums.GenericBundleKeys;
import com.ifma.appmhelp.models.Cid;
import com.ifma.appmhelp.models.Paciente;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CidActivity extends AppCompatActivity {

    private Paciente paciente;
    private Spinner spCids;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.paciente = (Paciente) getIntent().getSerializableExtra(GenericBundleKeys.PACIENTE.toString());
        this.carregarComponentes();
        this.inicializaAdapter();
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

    private void carregarComponentes(){
        this.spCids = (Spinner) findViewById(R.id.spCids);
    }

    private void inicializaAdapter(){
        ArrayList<Cid> listaDeCids = (ArrayList<Cid>) this.carregaCids();
        this.spCids.setAdapter(new CidsAdapter(this, listaDeCids));
    }

    private List<Cid> carregaCids(){
        try {
            return new CidDao(this).getTodos();
        } catch (SQLException e) {
            Toast.makeText(this, "Não foi possível carregar os cids: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return null;
        }
    }
}
