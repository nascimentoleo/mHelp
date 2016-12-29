package com.ifma.appmhelp.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.enums.EstadoCivil;
import com.ifma.appmhelp.enums.Sexo;
import com.ifma.appmhelp.enums.TipoSanguineo;

public class ProntuarioActivity extends AppCompatActivity {

    private Spinner spSexo;
    private Spinner spEstadoCivil;
    private Spinner spTipoSanguineo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prontuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.inicializaComponentes();
        this.carregaAdapters();
    }

    private void inicializaComponentes(){
        spSexo           = (Spinner) findViewById(R.id.spSexoProntuario);
        spEstadoCivil    = (Spinner) findViewById(R.id.spEstadoCivilProntuario);
        spTipoSanguineo  = (Spinner) findViewById(R.id.spTipoSanguineoProntuario);
    }

    private void carregaAdapters(){
        spSexo.setAdapter(new ArrayAdapter<Sexo>(this, android.R.layout.simple_spinner_item, Sexo.values()));
        spEstadoCivil.setAdapter(new ArrayAdapter<EstadoCivil>(this, android.R.layout.simple_spinner_item, EstadoCivil.values()));
        spTipoSanguineo.setAdapter(new ArrayAdapter<TipoSanguineo>(this, android.R.layout.simple_spinner_item, TipoSanguineo.values()));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;

    }

}
