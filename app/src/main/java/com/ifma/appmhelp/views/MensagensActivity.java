package com.ifma.appmhelp.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.enums.GenericBundleKeys;
import com.ifma.appmhelp.models.Ocorrencia;

public class MensagensActivity extends AppCompatActivity {

    private Ocorrencia ocorrencia;
    private ListView listMensagens;
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
    }

    private void carregaComponentes(){
        listMensagens = (ListView) findViewById(R.id.listMensagens);
        edMensagem    = (EditText) findViewById(R.id.edMensagem);
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
