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
import com.ifma.appmhelp.daos.MedicoPacienteDao;
import com.ifma.appmhelp.daos.MedicosDao;
import com.ifma.appmhelp.controls.MensagemController;
import com.ifma.appmhelp.controls.RosterXMPPController;
import com.ifma.appmhelp.daos.UsuariosDao;
import com.ifma.appmhelp.enums.StatusSolicitacaoRoster;
import com.ifma.appmhelp.enums.TipoDeMensagem;
import com.ifma.appmhelp.models.Medico;
import com.ifma.appmhelp.models.MedicoPaciente;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Paciente;
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
            Usuario usuarioMedico = (Usuario) intent.getSerializableExtra("usuario_medico");
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
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        enviaRespostaDaSolicitacao(StatusSolicitacaoRoster.APROVADA);
                        adicionarMedico(medico);
                    }})

                .setNegativeButton(android.R.string.no,new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enviaRespostaDaSolicitacao(StatusSolicitacaoRoster.REPROVADA);
                        Toast.makeText(AdicionarMedicoActivity.this, "Médico recusado! ", Toast.LENGTH_LONG).show();
                    }
                }).create();
    }

    private void enviaRespostaDaSolicitacao(StatusSolicitacaoRoster statusResposta){
        try {
            SolicitacaoRoster solicitacaoRoster = new SolicitacaoRoster(paciente.getUsuario(), statusResposta);
            Mensagem mensagem = new Mensagem(solicitacaoRoster.toJson(), TipoDeMensagem.SOLICITACAO_ROSTER);
            MensagemController.enviaMensagem(medico.getUsuario(), mensagem);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void adicionarMedico(Medico medico){
        try {
            RosterXMPPController roster = new RosterXMPPController();
            roster.addRoster(medico.getUsuario());
            UsuariosDao usuariosController  = new UsuariosDao(AdicionarMedicoActivity.this);
            MedicosDao medicosController    = new MedicosDao(AdicionarMedicoActivity.this);

            Usuario usuarioDB = usuariosController.getUsuarioByLogin(medico.getUsuario().getLogin());
            if (usuarioDB == null){
                medico.setId(null);
                medico.getUsuario().setId(null);
            }else{
                medico.getUsuario().setId(usuarioDB.getId());

                Medico medicoDB = medicosController.getMedicoByUsuario(usuarioDB);
                if(medicoDB != null){
                    medico.setId(medicoDB.getId());
                }

            }

            MedicoPaciente medicoPaciente = new MedicoPaciente(medico, paciente);
            new MedicoPacienteDao(AdicionarMedicoActivity.this).persistir(medicoPaciente, true);
            Toast.makeText(AdicionarMedicoActivity.this, "Médico adicionado! ", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
