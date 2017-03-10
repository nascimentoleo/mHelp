package com.ifma.appmhelp.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.adapters.MensagensAdapter;
import com.ifma.appmhelp.controls.MensagemController;
import com.ifma.appmhelp.controls.MensagemPagination;
import com.ifma.appmhelp.controls.Pagination;
import com.ifma.appmhelp.controls.RosterXMPPController;
import com.ifma.appmhelp.enums.GenericBundleKeys;
import com.ifma.appmhelp.enums.IntentType;
import com.ifma.appmhelp.enums.TipoDeMensagem;
import com.ifma.appmhelp.lib.EndlessRecyclerViewScrollListener;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Ocorrencia;
import com.ifma.appmhelp.models.Usuario;
import com.ifma.appmhelp.models.UsuarioLogado;

import org.jivesoftware.smack.SmackException;

import java.sql.SQLException;
import java.util.List;

public class MensagensActivity extends AppCompatActivity {

    private Ocorrencia ocorrencia;
    private RecyclerView rViewMensagens;
    private EditText edMensagem;
    private List<Mensagem> listaDeMensagens;
    private Pagination mensagemPagination;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Mensagem mensagem = (Mensagem) intent.getSerializableExtra(GenericBundleKeys.MENSAGEM.toString());
            if (!listaDeMensagens.contains(mensagem)){
                atualizarAdapter(mensagem);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagens);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ocorrencia = (Ocorrencia) getIntent().getSerializableExtra(GenericBundleKeys.OCORRENCIA.toString());
        personalizarToolbar(toolbar);
        carregaComponentes();
        inicializaAdapter();
    }

    private void personalizarToolbar(Toolbar toolbar){
        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        title.setText(ocorrencia.getTitulo());

        TextView subtitle      = (TextView) toolbar.findViewById(R.id.toolbar_subtitle);
        Usuario usuarioDestino = this.getUsuarioDestino(ocorrencia);
        subtitle.setText(usuarioDestino.getNome());

        try {
            if (new RosterXMPPController().rosterIsOnline(usuarioDestino))
                subtitle.append(" - " + getString(R.string.online_name));
            else
                subtitle.append(" - " + getString(R.string.offline_name));
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }


    }


    private void inicializaAdapter() {
        try {
            this.listaDeMensagens = mensagemPagination.getRegistros(Pagination.FIRST);
            MensagensAdapter adapter = new MensagensAdapter(this, listaDeMensagens);
            this.rViewMensagens.setAdapter(adapter);
            this.rViewMensagens.getLayoutManager().scrollToPosition(0);
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Não foi possível carregar as mensagens - " + e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void atualizarAdapter(Mensagem mensagem){
        listaDeMensagens.add(0,mensagem);
        rViewMensagens.getAdapter().notifyItemInserted(0);
        this.rViewMensagens.smoothScrollToPosition(0);
    }

    private void carregaComponentes(){
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(IntentType.ATUALIZAR_MENSAGENS.toString()));

        rViewMensagens = (RecyclerView) findViewById(R.id.rViewMensagens);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        rViewMensagens.setLayoutManager(linearLayoutManager);
        this.rViewMensagens.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(totalItemsCount);

            }
        });

        edMensagem        = (EditText) findViewById(R.id.edMensagem);

        mensagemPagination = new MensagemPagination(this,ocorrencia);
        mensagemPagination.setQtdDeRegistros(200);

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

    public void enviarMensagem(View v){
        if (this.mensagemEhValida()){
            try {
                Mensagem mensagem = new Mensagem(edMensagem.getText().toString().trim(), TipoDeMensagem.NOVA_MENSAGEM);
                mensagem.setOcorrencia(this.ocorrencia.clone());
                mensagem.setUsuario(UsuarioLogado.getInstance().getUsuario().clone());

                MensagemController controller = new MensagemController(this);
                controller.salvarMensagem(mensagem);

                edMensagem.getText().clear();
               // KeyboardLib.fecharTeclado(this);
                this.atualizarAdapter(mensagem);

                controller.enviaMensagem(this.getUsuarioDestino(mensagem.getOcorrencia()), mensagem);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this,
                        "Erro ao enviar mensagem: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

    }


    private boolean mensagemEhValida(){
        return !edMensagem.getText().toString().trim().isEmpty();
    }

    private Usuario getUsuarioDestino(Ocorrencia ocorrencia){
        if (UsuarioLogado.getInstance().getUsuario().equals(ocorrencia.getMedico().getUsuario()))
            return ocorrencia.getPaciente().getUsuario();
        return ocorrencia.getMedico().getUsuario();
    }

}
