package com.ifma.appmhelp.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.Medico;

public class AlteraDadosActivity extends AppCompatActivity{

    private IModel usuarioLogado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altera_dados);
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

        usuarioLogado = (IModel) this.getIntent().getExtras().getSerializable("usuarioLogado");
        Fragment fragment;
        if(usuarioLogado.getClass() == Medico.class)
            fragment = new AlteraMedicoFragment();
        else
            fragment = new AlteraPacienteFragment();
        fragment.setArguments(this.getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container_altera_dados,fragment).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent it = new Intent();
                it.putExtra("usuarioLogado",this.usuarioLogado);
                setResult(1,it);
                finish();
                return true;
        }
        return false;

    }
}
