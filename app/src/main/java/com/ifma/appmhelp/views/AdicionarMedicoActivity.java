package com.ifma.appmhelp.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ifma.appmhelp.R;
import com.ifma.appmhelp.controls.MensagemController;
import com.ifma.appmhelp.controls.ProntuariosController;
import com.ifma.appmhelp.controls.SolicitacoesController;
import com.ifma.appmhelp.enums.IntentType;
import com.ifma.appmhelp.enums.SolicitacaoBundleKeys;
import com.ifma.appmhelp.enums.StatusSolicitacaoRoster;
import com.ifma.appmhelp.enums.TipoDeMensagem;
import com.ifma.appmhelp.models.Medico;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.Prontuario;
import com.ifma.appmhelp.models.ProntuarioParaEnvio;
import com.ifma.appmhelp.models.SolicitacaoRoster;
import com.ifma.appmhelp.models.Usuario;
import com.ifma.appmhelp.models.UsuarioLogado;

public class AdicionarMedicoActivity extends AppCompatActivity {

    private ImageView imgQrCode;
    private Paciente paciente;
    private Medico medico;


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Usuario usuarioMedico = (Usuario) intent.getSerializableExtra(SolicitacaoBundleKeys.USUARIO_MEDICO.toString());
            medico = new Medico(usuarioMedico);
            //Precisa rodar dentro da thread para evitar que seja executado com a Activity em background
            runOnUiThread(new Runnable() {
                              @Override
                              public void run() {
                                  if(!isFinishing())
                                    createDialog().show();
                              }
                          });

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_medico);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(IntentType.SOLICITACAO_ROSTER.toString()));
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
            pacienteParaEnvio.setUsuario(this.paciente.getUsuario().clone());
            pacienteParaEnvio.getUsuario().setSenha(null); //Limpo a senha antes de enviar
            pacienteParaEnvio.setOcorrencias(null); //Ocorrencias tambem são limpas
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


    private AlertDialog createDialog(){

        return new AlertDialog.Builder(AdicionarMedicoActivity.this)
                .setTitle("Um médico deseja lhe adicionar")
                .setMessage("Confirmar solicitação de "+ medico.getUsuario().getNome())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.resposta_sim, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        enviaRespostaDaSolicitacao(StatusSolicitacaoRoster.APROVADA);
                        enviaProntuario(medico.getUsuario(),paciente.getProntuario());
                        adicionarMedico(medico);
                    }})

                .setNegativeButton(R.string.resposta_nao,new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enviaRespostaDaSolicitacao(StatusSolicitacaoRoster.REPROVADA);
                        Snackbar.make(findViewById(android.R.id.content), "Médico recusado", Snackbar.LENGTH_LONG).show();
                    }
                }).create();
    }

    private void enviaRespostaDaSolicitacao(StatusSolicitacaoRoster statusResposta){
        try {
            SolicitacaoRoster solicitacaoRoster = new SolicitacaoRoster(paciente.getUsuario().clone(), statusResposta);
            Mensagem mensagem = new Mensagem(solicitacaoRoster.toJson(), TipoDeMensagem.SOLICITACAO_ROSTER);
            MensagemController.enviaMensagem(medico.getUsuario(), mensagem);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao enviar confirmação ao médico: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void enviaProntuario(Usuario usuarioTo, Prontuario prontuario){
        try {
            ProntuarioParaEnvio prontuarioParaEnvio = new ProntuariosController(this).getProntuarioParaEnvio(prontuario);
            Mensagem mensagem = new Mensagem(prontuarioParaEnvio.toJson(), TipoDeMensagem.ATUALIZACAO_PRONTUARIO);
            MensagemController.enviaMensagem(usuarioTo, mensagem);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao enviar o prontuário ao médico: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void adicionarMedico(Medico medico){
        try {
            SolicitacoesController.adicionarUsuario(this, paciente, medico);
            Snackbar.make(findViewById(android.R.id.content), "Médico adicionado", Snackbar.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao adicionar médico: " + e.getMessage(), Toast.LENGTH_LONG).show();

        }
    }

}
