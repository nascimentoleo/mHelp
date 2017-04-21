package com.ifma.appmhelp.views;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.adapters.MensagensAdapter;
import com.ifma.appmhelp.controls.AnexoController;
import com.ifma.appmhelp.controls.MensagemController;
import com.ifma.appmhelp.controls.MensagemPagination;
import com.ifma.appmhelp.controls.Pagination;
import com.ifma.appmhelp.controls.RosterXMPPController;
import com.ifma.appmhelp.enums.AnexoIntent;
import com.ifma.appmhelp.enums.GenericBundleKeys;
import com.ifma.appmhelp.enums.IntentType;
import com.ifma.appmhelp.enums.RequestType;
import com.ifma.appmhelp.enums.TipoAnexo;
import com.ifma.appmhelp.enums.TipoDeMensagem;
import com.ifma.appmhelp.lib.EndlessRecyclerViewScrollListener;
import com.ifma.appmhelp.models.Anexo;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Ocorrencia;
import com.ifma.appmhelp.models.Usuario;
import com.ifma.appmhelp.models.UsuarioLogado;

import org.jivesoftware.smack.SmackException;

import java.sql.SQLException;
import java.util.List;

public class MensagensActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private static Ocorrencia ocorrencia;
    private RecyclerView rViewMensagens;
    private EditText edMensagem;
    private List<Mensagem> listaDeMensagens;
    private Pagination mensagemPagination;
    private static boolean active = false;
    private Uri anexoUri;
    private TipoAnexo tipoAnexoParaEnvio;

    private BroadcastReceiver mReceiverMensagem = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Mensagem mensagem = (Mensagem) intent.getSerializableExtra(GenericBundleKeys.MENSAGEM.toString());

            //Atualiza uma mensagem específica
            if (mensagem != null){
                if (ocorrencia.equals(mensagem.getOcorrencia()))
                    if (!listaDeMensagens.contains(mensagem))
                        atualizarAdapter(mensagem);

            //Atualiza todas as mensagems
            }else
                 atualizarAdapter();

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

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
        Intent it = new Intent(IntentType.LIMPAR_MENSAGEM.toString());
        it.putExtra(GenericBundleKeys.OCORRENCIA.toString(), ocorrencia);
        LocalBroadcastManager.getInstance(this).sendBroadcast(it);

    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }

    public static boolean isActive() {
        return active;
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(this);
        inflater.inflate(R.menu.mensagem, popup.getMenu());
        popup.show();
    }

    public static Ocorrencia getOcorrencia() {
        return ocorrencia;
    }

    private void personalizarToolbar(Toolbar toolbar) {
        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        title.setText(ocorrencia.getTitulo());

        TextView subtitle = (TextView) toolbar.findViewById(R.id.toolbar_subtitle);
        Usuario usuarioDestino = this.getUsuarioDestino(ocorrencia);
        subtitle.setText(usuarioDestino.getNome());

        try {
            if (RosterXMPPController.getInstance().rosterIsOnline(usuarioDestino))
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
            adapter.setOnItemLongClickListener(new AbrirAnexo());
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Não foi possível carregar as mensagens - " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void atualizarAdapter(Mensagem mensagem) {
        listaDeMensagens.add(0, mensagem);
        rViewMensagens.getAdapter().notifyItemInserted(0);
        this.rViewMensagens.smoothScrollToPosition(0);
    }

    private void atualizarAdapter(){
        rViewMensagens.getAdapter().notifyDataSetChanged();
    }

    private void carregaComponentes() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiverMensagem, new IntentFilter(IntentType.ATUALIZAR_MENSAGENS.toString()));

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


        edMensagem = (EditText) findViewById(R.id.edMensagem);

        mensagemPagination = new MensagemPagination(this, ocorrencia);
        mensagemPagination.setQtdDeRegistros(200);

        findViewById(R.id.toolbar_title).setSelected(true);

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
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
           case R.id.action_EnviarImagem:
               selecionarAnexo(TipoAnexo.IMAGEM);
               break;
           case R.id.action_EnviarVideo:
               selecionarAnexo(TipoAnexo.VIDEO);
               break;

        }
        return false;
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

    public void enviarMensagem(View v) {
        if (this.mensagemEhValida()) {
            Mensagem mensagem = new Mensagem(edMensagem.getText().toString().trim(), TipoDeMensagem.NOVA_MENSAGEM);
            this.enviarMensagem(mensagem);
        }

    }

    private void enviarMensagem(Mensagem mensagem){
        try {
            mensagem.setOcorrencia(this.ocorrencia.clone());
            mensagem.setUsuario(UsuarioLogado.getInstance().getUsuario().clone());

            MensagemController controller = new MensagemController(this);
            controller.salvarMensagem(mensagem);

            edMensagem.getText().clear();
            this.atualizarAdapter(mensagem);

            controller.enviaMensagem(this.getUsuarioDestino(mensagem.getOcorrencia()), mensagem);


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,
                "Erro ao enviar mensagem: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private boolean mensagemEhValida() {
        return !edMensagem.getText().toString().trim().isEmpty();
    }

    private Usuario getUsuarioDestino(Ocorrencia ocorrencia) {
        if (UsuarioLogado.getInstance().getUsuario().equals(ocorrencia.getMedico().getUsuario()))
            return ocorrencia.getPaciente().getUsuario();
        return ocorrencia.getMedico().getUsuario();
    }

    private void selecionarAnexo(final TipoAnexo tipoAnexo) {
        final CharSequence[] items = {AnexoIntent.CAMERA.toString(),
                AnexoIntent.GALERIA.toString(),
                AnexoIntent.CANCELAR.toString()};
        AlertDialog.Builder builder = new AlertDialog.Builder(MensagensActivity.this);
        builder.setTitle("Selecionar " + tipoAnexo.toString());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(AnexoIntent.CANCELAR.toString()))
                    dialog.dismiss();
                else{
                    if (items[item].equals(AnexoIntent.GALERIA.toString()))
                        createIntent(tipoAnexo, AnexoIntent.GALERIA);
                    else
                        createIntent(tipoAnexo, AnexoIntent.CAMERA);

                }
            }
        });
        builder.show();
    }

    private void createIntent(TipoAnexo tipoAnexo, AnexoIntent anexoIntent){
        this.tipoAnexoParaEnvio = tipoAnexo;

        if (anexoIntent == AnexoIntent.CAMERA) {
            int permissionCamera = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA);

            if (permissionCamera == PermissionChecker.PERMISSION_GRANTED)
                createCameraIntent(tipoAnexo);
        }
        else {
            int permissionReadExternal = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);

            if (permissionReadExternal == PermissionChecker.PERMISSION_GRANTED)
                createGalleryIntent(tipoAnexo);
        }

    }

    private void createGalleryIntent(TipoAnexo tipoAnexo) {
        Intent intent = new Intent();
        String msg;
        if (tipoAnexo == TipoAnexo.IMAGEM) {
            intent.setType("image/*");
            msg = "Selecione uma imagem";
        }else {
            intent.setType("video/*");
            msg = "Selecione um vídeo";
        }
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, msg), RequestType.ABRIR_GALERIA.getValue());

    }

    private void createCameraIntent(TipoAnexo tipoAnexo) {
        Intent intent;

        if (tipoAnexo == TipoAnexo.IMAGEM) {
            intent   = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            ContentValues values = new ContentValues();

            //Precisa especificar onde irá salvar
            anexoUri = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            values.put(MediaStore.Images.Media.TITLE, "IMG_" + System.currentTimeMillis());
            values.put(MediaStore.Images.Media.DESCRIPTION, "From mHelp");

            intent.putExtra(MediaStore.EXTRA_OUTPUT, anexoUri);
        }
        else
            intent  = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        startActivityForResult(intent, RequestType.ABRIR_CAMERA.getValue());

    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String nomeArquivo;
            try {
                AnexoController anexoController = new AnexoController(MensagensActivity.this);

                if (requestCode == RequestType.ABRIR_GALERIA.getValue())
                    nomeArquivo = anexoController.enviarArquivo(this.tipoAnexoParaEnvio, data.getData());
                else if (anexoUri != null)
                    nomeArquivo = anexoController.enviarArquivo(this.tipoAnexoParaEnvio, this.anexoUri);
                else
                    nomeArquivo = anexoController.enviarArquivo(this.tipoAnexoParaEnvio, data.getData());

                Mensagem mensagem = new Mensagem();
                mensagem.setTipo(TipoDeMensagem.NOVA_MENSAGEM);
                mensagem.setAnexo(new Anexo(nomeArquivo, this.tipoAnexoParaEnvio));
                enviarMensagem(mensagem);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MensagensActivity.this, "Erro ao enviar imagem " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class AbrirAnexo implements MensagensAdapter.OnItemClickListener{

        @Override
        public void onItemClick(Mensagem item) {
            if (item.getAnexo() != null){
                Intent intent = new Intent(MensagensActivity.this, AnexoActivity.class);
                intent.putExtra(GenericBundleKeys.ANEXO.toString(),item.getAnexo());
                startActivity(intent);
            }
        }
    }
}
