package com.ifma.appmhelp.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ifma.appmhelp.R;
import com.ifma.appmhelp.controls.MensagemController;
import com.ifma.appmhelp.controls.RosterXMPPController;
import com.ifma.appmhelp.enums.StatusSolicitacaoRoster;
import com.ifma.appmhelp.enums.TipoDeMensagem;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.SolicitacaoRoster;
import com.ifma.appmhelp.models.Usuario;
import com.ifma.appmhelp.models.UsuarioLogado;

import org.jivesoftware.smack.SmackException;

public class AdicionarMedicoActivity extends AppCompatActivity {

    private ImageView imgQrCode;
    private Paciente paciente;
    private Usuario usuario;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            usuario = (Usuario) intent.getSerializableExtra("usuario");

            new AlertDialog.Builder(AdicionarMedicoActivity.this)
            .setTitle("Um médico deseja lhe adicionar")
            .setMessage("Confirmar solicitação de "+ usuario.getNome())
            .setIcon(android.R.drawable.ic_dialog_alert)

            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    try {
                        RosterXMPPController roster = new RosterXMPPController();
                        roster.addRoster(usuario);
                        //Envia confirmação
                        SolicitacaoRoster solicitacaoRoster = new SolicitacaoRoster(paciente.getUsuario(), StatusSolicitacaoRoster.APROVADA);
                        Mensagem mensagem = new Mensagem(solicitacaoRoster.toJson(), TipoDeMensagem.SOLICITACAO_ROSTER);
                        MensagemController.enviaMensagem(usuario, mensagem);
                        Toast.makeText(AdicionarMedicoActivity.this, "Médico adicionado! ", Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }})

            .setNegativeButton(android.R.string.no,new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        SolicitacaoRoster solicitacaoRoster = new SolicitacaoRoster(paciente.getUsuario(), StatusSolicitacaoRoster.REPROVADA);
                        Mensagem mensagem = new Mensagem(solicitacaoRoster.toJson(), TipoDeMensagem.SOLICITACAO_ROSTER);
                        MensagemController.enviaMensagem(usuario, mensagem);
                        Toast.makeText(AdicionarMedicoActivity.this, "Médico recusado! ", Toast.LENGTH_LONG).show();
                    } catch (SmackException.NotConnectedException e) {
                        e.printStackTrace();
                    }
                }
            }).show();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_medico);
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
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("solicitacao_roster"));
        this.carregaComponentes();
        this.exibeQRCode();

    }

    private void carregaComponentes(){
        this.imgQrCode = (ImageView) findViewById(R.id.imgQrCode);
        this.paciente = (Paciente) UsuarioLogado.getInstance().getModelo();

    }

    private void exibeQRCode(){
        //Calcula o tamanho que o qrcode deve ocupar
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int altura  = metrics.heightPixels/2;
        int largura = metrics.widthPixels;

        try {
            //Vou retirar a senha antes de gerar o qrcode
            Paciente pacienteParaEnvio = this.paciente.clone();
            pacienteParaEnvio.getUsuario().setSenha("");
            BitMatrix bitMatrix = new QRCodeWriter().encode(pacienteParaEnvio.toJson(), BarcodeFormat.QR_CODE,largura,altura);
            Bitmap bmp = Bitmap.createBitmap(largura, altura, Bitmap.Config.RGB_565);
            for (int i = 0; i < largura; i ++)
                for (int j = 0; j < altura; j ++)
                    bmp.setPixel(i, j, bitMatrix.get(i,j) ? Color.BLACK : Color.WHITE);

            imgQrCode.setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

}
