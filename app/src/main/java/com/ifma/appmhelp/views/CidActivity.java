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
import com.ifma.appmhelp.daos.CidDao;
import com.ifma.appmhelp.daos.ProntuarioCidDao;
import com.ifma.appmhelp.enums.GenericBundleKeys;
import com.ifma.appmhelp.lib.EndlessRecyclerViewScrollListener;
import com.ifma.appmhelp.models.Cid;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.ProntuarioCid;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CidActivity extends AppCompatActivity implements CidsAdapter.OnItemLongClickListener {

    private Paciente paciente;
    private RecyclerView rViewCids;
    private RecyclerView rViewCidsCadastrados;
    private EditText edCidCodigo;
    private EditText edCidDescricao;
    private TextView txtCidNotFound;
    private int qtdRegistros = 20; //Quantidade de registros por refresh do adapter
    private ArrayList<Cid> adapterCid;
    private ArrayList<Cid> adapterCidCadastrados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.paciente = (Paciente) getIntent().getSerializableExtra(GenericBundleKeys.PACIENTE.toString());
        this.carregaComponentes();
        this.carregarCidsDoProntuario();
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

    private void carregarCidsDoProntuario(){
        if(this.paciente != null){
            this.adapterCidCadastrados.clear();
            try {
                List<ProntuarioCid> prontuarioCidList = new ProntuarioCidDao(this).getProntuariosCids(this.paciente.getProntuario());
                exibeErroCidNotFound(prontuarioCidList.isEmpty());
                for(ProntuarioCid prontuarioCid: prontuarioCidList) {
                    this.adapterCidCadastrados.add(prontuarioCid.getCid());
                    this.paciente.getProntuario().getCids().add(prontuarioCid.getCid());
                }
                rViewCidsCadastrados.getAdapter().notifyDataSetChanged();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void adicionarProntuarioCid(ProntuarioCid prontuarioCid){
        try {
            new ProntuarioCidDao(this).persistir(prontuarioCid,false);
            this.paciente.getProntuario().getCids().add(prontuarioCid.getCid());
            this.adapterCidCadastrados.add(prontuarioCid.getCid());
            rViewCidsCadastrados.getAdapter().notifyDataSetChanged();
            Snackbar.make(findViewById(android.R.id.content), "Cid adicionado", Snackbar.LENGTH_LONG).show();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao inserir cid: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

        this.adapterCid = new ArrayList<>();
        this.adapterCidCadastrados = new ArrayList<>();

        CidsAdapter cidsAdapter            = new CidsAdapter(this, adapterCid);
        CidsAdapter cidsAdapterCadastrados = new CidsAdapter(this, adapterCidCadastrados);

        cidsAdapter.setOnItemLongClickListener(this);
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
     }

    private void atualizaAdapter(int inicio, int fim){
        try {
            List<Cid> listCids = null;
            CidDao dao = new CidDao(this);

            if(!edCidDescricao.getText().toString().isEmpty()) {
                listCids = dao.getCidsByField(Long.valueOf(inicio), Long.valueOf(fim),
                        "descricao", edCidDescricao.getText().toString().trim());
            }
            else if(!edCidCodigo.getText().toString().isEmpty()){
                listCids = dao.getCidsByField(Long.valueOf(inicio), Long.valueOf(fim),
                        "codigo", edCidCodigo.getText().toString().trim());
            }else
                listCids = dao.getCids(Long.valueOf(inicio),Long.valueOf(fim));

            adapterCid.addAll(listCids);
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
        atualizaAdapter(offset, offset + qtdRegistros);
    }

    @Override
    public void onItemLongClick(Cid item) {
        ProntuarioCid prontuarioCid = new ProntuarioCid(this.paciente.getProntuario(), item);
        this.adicionarProntuarioCid(prontuarioCid);
    }

    class OnEditorActionListener implements EditText.OnEditorActionListener{

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_GO     || actionId == EditorInfo.IME_ACTION_NEXT ||
                event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    adapterCid.clear();
                    atualizaAdapter(0,qtdRegistros);
                    return true;
            }
            return false;
        }
    }


}
