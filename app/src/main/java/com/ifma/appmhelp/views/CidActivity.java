package com.ifma.appmhelp.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.adapters.CidsAdapter;
import com.ifma.appmhelp.controls.CidPagination;
import com.ifma.appmhelp.controls.Pagination;
import com.ifma.appmhelp.daos.ProntuarioCidDao;
import com.ifma.appmhelp.enums.GenericBundleKeys;
import com.ifma.appmhelp.lib.EndlessRecyclerViewScrollListener;
import com.ifma.appmhelp.lib.KeyboardLib;
import com.ifma.appmhelp.lib.ModelComparator;
import com.ifma.appmhelp.models.Cid;
import com.ifma.appmhelp.models.Prontuario;
import com.ifma.appmhelp.models.ProntuarioCid;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CidActivity extends AppCompatActivity {

    private Prontuario prontuario;
    private RecyclerView rViewCids;
    private RecyclerView rViewCidsCadastrados;
    private EditText edCidCodigo;
    private EditText edCidDescricao;
    private TextView txtCidNotFound;
    private CidPagination pagination;
    private ArrayList<Cid> cidsDisponiveis;
    private ArrayList<Cid> cidsCadastrados;
    private boolean modificouProntuario;
    private boolean permiteEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.prontuario = (Prontuario) getIntent().getSerializableExtra(GenericBundleKeys.PRONTUARIO.toString());
        this.permiteEditar = getIntent().getBooleanExtra(GenericBundleKeys.EDITAR_PRONTUARIO.toString(), false);

        this.carregaComponentes();
        this.carregarCidsDoProntuario();
        this.atualizaAdapter(Pagination.FIRST);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent returnIntent = new Intent();
                returnIntent.putExtra(GenericBundleKeys.PRONTUARIO.toString(),prontuario);
                returnIntent.putExtra(GenericBundleKeys.MODIFICOU_PRONTUARIO.toString(), modificouProntuario);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
                return true;
        }
        return false;
    }

    private void carregarCidsDoProntuario(){
        if(this.prontuario != null){
            this.cidsCadastrados.clear();
            try {
                List<ProntuarioCid> prontuarioCidList = new ProntuarioCidDao(this).getProntuariosCids(this.prontuario);
                exibeErroCidNotFound(prontuarioCidList.isEmpty());
                for(ProntuarioCid prontuarioCid: prontuarioCidList) {
                    this.cidsCadastrados.add(prontuarioCid.getCid());
                }
                rViewCidsCadastrados.getAdapter().notifyDataSetChanged();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void exibeErroCidNotFound(boolean mostraErro){
        if (mostraErro){
            txtCidNotFound.setVisibility(View.VISIBLE);
            txtCidNotFound.setPadding(0,10,0,10);
            txtCidNotFound.setTextSize(17);
        }else{
            txtCidNotFound.setVisibility(View.INVISIBLE);
            txtCidNotFound.setPadding(0,0,0,0);
            txtCidNotFound.setTextSize(0);
        }

    }

    private void carregaComponentes(){

        this.edCidCodigo = (EditText) findViewById(R.id.edCidCodigo);
        this.edCidDescricao = (EditText) findViewById(R.id.edCidDescricao);
        this.txtCidNotFound = (TextView) findViewById(R.id.txtCidNotFound);

        this.rViewCids = (RecyclerView) findViewById(R.id.rViewCids);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        this.rViewCids.setLayoutManager(linearLayoutManager);
        this.rViewCids.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(totalItemsCount);

            }
        });

        this.rViewCidsCadastrados = (RecyclerView) findViewById(R.id.rViewCidsCadastrados);
        this.rViewCidsCadastrados.setLayoutManager(new LinearLayoutManager(this));

        this.cidsDisponiveis = new ArrayList<>();
        this.cidsCadastrados = new ArrayList<>();

        CidsAdapter cidsAdapter            = new CidsAdapter(this, cidsDisponiveis);
        CidsAdapter cidsAdapterCadastrados = new CidsAdapter(this, cidsCadastrados);

        //Só habilita os eventos se o prontuário for aberto em modo de edição
        if(this.permiteEditar){
            cidsAdapter.setOnItemLongClickListener(new AdicionarCidListener());
            cidsAdapterCadastrados.setOnItemLongClickListener(new RemoverCidListener());
        }

        this.rViewCids.setAdapter(cidsAdapter);

        this.rViewCidsCadastrados.setAdapter(cidsAdapterCadastrados);

        this.edCidCodigo.setOnEditorActionListener(new OnEditorActionListener());
        this.edCidDescricao.setOnEditorActionListener(new OnEditorActionListener());

        this.edCidCodigo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    limparCamposDeBusca();
                }
            }
        });

        this.edCidDescricao.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    limparCamposDeBusca();
                }
            }
        });
        modificouProntuario = false;
        this.pagination = new CidPagination(this);
     }

    private void atualizaAdapter(int offset){
        try {
            List<Cid> listCids;

            if(!edCidDescricao.getText().toString().isEmpty())
                listCids = pagination.getRegistros(offset, "descricao", edCidDescricao.getText().toString().trim());

            else if(!edCidCodigo.getText().toString().isEmpty())
                listCids = pagination.getRegistros(offset,"codigo", edCidCodigo.getText().toString().trim());

            else
                listCids = pagination.getRegistros(offset);

            cidsDisponiveis.addAll(listCids);
            //Excluo os cids já cadastrados da lista de cids disponíveis
            cidsDisponiveis.removeAll(cidsCadastrados);
            rViewCids.getAdapter().notifyDataSetChanged();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void limparCamposDeBusca(){
        this.edCidCodigo.getText().clear();
        this.edCidDescricao.getText().clear();
    }

    public void loadNextDataFromApi(int offset) {
        atualizaAdapter(offset);
    }

    class OnEditorActionListener implements EditText.OnEditorActionListener{

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_GO     || actionId == EditorInfo.IME_ACTION_NEXT ||
                event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    cidsDisponiveis.clear();
                    atualizaAdapter(Pagination.FIRST);
                    return true;
            }
            return false;
        }
    }

    class AdicionarCidListener implements CidsAdapter.OnItemLongClickListener{

        @Override
        public void onItemLongClick(Cid item) {
            ProntuarioCid prontuarioCid = new ProntuarioCid(prontuario, item);
            this.adicionarProntuarioCid(prontuarioCid);

        }

        private void adicionarProntuarioCid(ProntuarioCid prontuarioCid){
            try {
                new ProntuarioCidDao(getApplicationContext()).persistir(prontuarioCid,false);

                //Adiciona o cid na lista de cadastrados, e remove da lista de disponíveis
                cidsCadastrados.add(prontuarioCid.getCid());
                rViewCidsCadastrados.getAdapter().notifyDataSetChanged();
                exibeErroCidNotFound(cidsCadastrados.isEmpty());

                cidsDisponiveis.remove(prontuarioCid.getCid());
                rViewCids.getAdapter().notifyDataSetChanged();

                modificouProntuario = true;
                KeyboardLib.fecharTeclado(CidActivity.this);

                Snackbar.make(findViewById(android.R.id.content), "Cid adicionado", Snackbar.LENGTH_LONG).show();
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),
                               "Erro ao inserir cid: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

    }

    class RemoverCidListener implements CidsAdapter.OnItemLongClickListener{

        @Override
        public void onItemLongClick(Cid item) {
            ProntuarioCid prontuarioCid = new ProntuarioCid(prontuario, item);
            this.removerProntuarioCid(prontuarioCid);

        }

        private void removerProntuarioCid(ProntuarioCid prontuarioCid){
            try {
                ProntuarioCidDao dao = new ProntuarioCidDao(getApplicationContext());
                dao.carregaId(prontuarioCid);
                dao.remover(prontuarioCid,false);

                //Remove o cid da lista de cadastrados, e adiciona na lista de disponíveis
                cidsCadastrados.remove(prontuarioCid.getCid());
                rViewCidsCadastrados.getAdapter().notifyDataSetChanged();
                exibeErroCidNotFound(cidsCadastrados.isEmpty());
                cidsDisponiveis.add(prontuarioCid.getCid());
                Collections.sort(cidsDisponiveis,new ModelComparator());

                rViewCids.getAdapter().notifyDataSetChanged();

                modificouProntuario = true;
                KeyboardLib.fecharTeclado(CidActivity.this);

                Snackbar.make(findViewById(android.R.id.content), "Cid removido", Snackbar.LENGTH_LONG).show();
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        "Erro ao remover cid: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }




}
