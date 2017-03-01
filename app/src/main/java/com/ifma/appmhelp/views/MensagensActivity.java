package com.ifma.appmhelp.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.adapters.MensagensAdapter;
import com.ifma.appmhelp.enums.GenericBundleKeys;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Ocorrencia;
import com.ifma.appmhelp.models.Usuario;

import java.util.ArrayList;
import java.util.List;

public class MensagensActivity extends AppCompatActivity {

    private Ocorrencia ocorrencia;
    private ListView listViewMensagens;
    private EditText edMensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagens);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ocorrencia = (Ocorrencia) getIntent().getSerializableExtra(GenericBundleKeys.OCORRENCIA.toString());
        carregaComponentes();
        inicializaAdapter();
    }

    private void inicializaAdapter() {
        List<Mensagem> listaDeMensagens = new ArrayList<>(); //As mensagens virão do banco
        listaDeMensagens.add(new Mensagem("Eae mano blz", new Usuario("paciente")));
        listaDeMensagens.add(new Mensagem("Tudo tranquilo", new Usuario("medico")));

        MensagensAdapter adapter = new MensagensAdapter(this, listaDeMensagens);
        listViewMensagens.setAdapter(adapter);

    }

    private void carregaComponentes(){
        listViewMensagens = (ListView) findViewById(R.id.listMensagens);
        edMensagem        = (EditText) findViewById(R.id.edMensagem);
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
