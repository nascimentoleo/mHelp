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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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
import com.ifma.appmhelp.enums.CameraIntent;
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

public class MensagensActivity extends AppCompatActivity {

    private static Ocorrencia ocorrencia;
    private RecyclerView rViewMensagens;
    private EditText edMensagem;
    private List<Mensagem> listaDeMensagens;
    private Pagination mensagemPagination;
    private static boolean active = false;
    private Uri imageUri;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Mensagem mensagem = (Mensagem) intent.getSerializableExtra(GenericBundleKeys.MENSAGEM.toString());
            if (ocorrencia.equals(mensagem.getOcorrencia()))
                if (!listaDeMensagens.contains(mensagem))
                    atualizarAdapter(mensagem);
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
//        ocorrencia = null; dando problema
    }

    public static boolean isActive() {
        return active;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mensagem, menu);
        return true;
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

    private void carregaComponentes() {
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

        edMensagem = (EditText) findViewById(R.id.edMensagem);

        mensagemPagination = new MensagemPagination(this, ocorrencia);
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
            case R.id.action_EnviarImagem:
                selecionarImagem();
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

    private void selecionarImagem() {
        final CharSequence[] items = {CameraIntent.CAMERA.toString(),
                CameraIntent.GALERIA.toString(),
                CameraIntent.CANCELAR.toString()};
        AlertDialog.Builder builder = new AlertDialog.Builder(MensagensActivity.this);
        builder.setTitle("Enviar Imagem");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(CameraIntent.CANCELAR.toString()))
                    dialog.dismiss();
                else{
                    if (items[item].equals(CameraIntent.GALERIA.toString()))
                        createImageIntent(CameraIntent.GALERIA);
                    else
                        createImageIntent(CameraIntent.CAMERA);

                }
            }
        });
        builder.show();
    }

    private void createImageIntent(CameraIntent cameraIntent) {
        Intent intent;
        if (cameraIntent == CameraIntent.CAMERA) {
            int permissionCheck = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA);

            if(permissionCheck == PermissionChecker.PERMISSION_GRANTED){
                //imageUri = Uri.fromFile(new AnexoController(this).getDiretorioParaCamera());

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "IMG_" + System.currentTimeMillis());
                values.put(MediaStore.Images.Media.DESCRIPTION, "From mHelp");

                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                startActivityForResult(intent, RequestType.ABRIR_CAMERA.getValue());
            }

        } else {
            int permissionCheck = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);

            if(permissionCheck == PermissionChecker.PERMISSION_GRANTED) {
                intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), RequestType.ABRIR_GALERIA.getValue());
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            AnexoController anexoController = new AnexoController(this);
            String nomeArquivo;
            try {

                if (requestCode == RequestType.ABRIR_GALERIA.getValue())
                    nomeArquivo = anexoController.enviarArquivo(data.getData());
                else {
                    nomeArquivo = anexoController.enviarArquivo(imageUri);

                }
                Mensagem mensagem = new Mensagem("Imagem", TipoDeMensagem.NOVA_MENSAGEM);
                mensagem.setAnexo(new Anexo(nomeArquivo, TipoAnexo.IMAGEM));
                this.enviarMensagem(mensagem);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this,"Erro ao enviar imagem " + e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }


    }


}
