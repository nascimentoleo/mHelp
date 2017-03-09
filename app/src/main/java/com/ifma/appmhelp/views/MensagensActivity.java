package com.ifma.appmhelp.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.adapters.MensagensAdapter;
import com.ifma.appmhelp.controls.MensagemPagination;
import com.ifma.appmhelp.controls.Pagination;
import com.ifma.appmhelp.enums.GenericBundleKeys;
import com.ifma.appmhelp.lib.EndlessRecyclerViewScrollListener;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Ocorrencia;

import java.sql.SQLException;
import java.util.List;

public class MensagensActivity extends AppCompatActivity {

    private Ocorrencia ocorrencia;
    private RecyclerView rViewMensagens;
    private EditText edMensagem;
    private List<Mensagem> listaDeMensagens;
    private Pagination mensagemPagination;

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
        try {
            this.listaDeMensagens = mensagemPagination.getRegistros(Pagination.FIRST);
            MensagensAdapter adapter = new MensagensAdapter(this, listaDeMensagens);
            this.rViewMensagens.setAdapter(adapter);

        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Não foi possível carregar as mensagens - " + e.getMessage(),Toast.LENGTH_SHORT).show();
        }

       /* listaDeMensagens = new ArrayList<>(); //As mensagens virão do banco
        listaDeMensagens.add(new Mensagem("Eae mano blz", new Usuario("paciente")));
        listaDeMensagens.add(new Mensagem("Tudo tranquilo", new Usuario("medico")));

        MensagensAdapter adapter = new MensagensAdapter(this, listaDeMensagens);
        rViewMensagens.setAdapter(adapter); */
    }

    private void carregaComponentes(){
        rViewMensagens = (RecyclerView) findViewById(R.id.rViewMensagens);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rViewMensagens.setLayoutManager(linearLayoutManager);
        this.rViewMensagens.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(totalItemsCount);

            }
        });

        edMensagem        = (EditText) findViewById(R.id.edMensagem);

        mensagemPagination = new MensagemPagination(this,ocorrencia);
        mensagemPagination.setQtdDeRegistros(50);

    }

    private void loadNextDataFromApi(int totalItemCount) {
        try {
            listaDeMensagens.addAll(mensagemPagination.getRegistros(totalItemCount));
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this,
                    "Erro ao carregar ocorrências: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        rViewMensagens.getAdapter().notifyDataSetChanged();
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
