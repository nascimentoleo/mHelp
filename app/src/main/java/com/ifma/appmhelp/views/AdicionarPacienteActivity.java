package com.ifma.appmhelp.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ifma.appmhelp.R;
import com.ifma.appmhelp.controls.MensagemController;
import com.ifma.appmhelp.controls.ProntuarioController;
import com.ifma.appmhelp.controls.SolicitacoesController;
import com.ifma.appmhelp.enums.IntentType;
import com.ifma.appmhelp.enums.SolicitacaoBundleKeys;
import com.ifma.appmhelp.enums.StatusSolicitacaoRoster;
import com.ifma.appmhelp.enums.TipoDeMensagem;
import com.ifma.appmhelp.models.Medico;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.PacienteParaEnvio;
import com.ifma.appmhelp.models.SolicitacaoRoster;
import com.ifma.appmhelp.models.UsuarioLogado;

import java.sql.SQLException;

public class AdicionarPacienteActivity extends AppCompatActivity {

    private Medico medico;
    private PacienteParaEnvio pacienteParaEnvio;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra(SolicitacaoBundleKeys.FINALIZOU.toString(), false)) {
                if (intent.getBooleanExtra(SolicitacaoBundleKeys.ACEITOU_SOLICITACAO.toString(), false)) {
                    if (pacienteParaEnvio.getPaciente() != null){
                        adicionarPaciente(pacienteParaEnvio.getPaciente());
                        if (pacienteParaEnvio.getProntuarioParaEnvio() != null)
                            atualizarProntuario(pacienteParaEnvio);

                    }else
                        Toast.makeText(AdicionarPacienteActivity.this,"Não foi possível ler o QRCode, tente novamente",Toast.LENGTH_SHORT).show();

                }else
                    Toast.makeText(AdicionarPacienteActivity.this,"Paciente recusou a solicitação",Toast.LENGTH_SHORT).show();

                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_paciente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(IntentType.SOLICITACAO_ROSTER.toString()));
        this.medico = (Medico) UsuarioLogado.getInstance().getModelo();

        this.iniciaQrScan();
    }

    private void iniciaQrScan() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Posicione a camera para escanear o código do paciente");
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() != null) {
                try {
                    this.pacienteParaEnvio = new PacienteParaEnvio().fromJson(intentResult.getContents());
                    Log.d("QRCode Paciente",this.pacienteParaEnvio.toJson());
                    SolicitacaoRoster solicitacaoRoster = new SolicitacaoRoster(medico.getUsuario().clone(), StatusSolicitacaoRoster.ENVIADA);
                    Mensagem mensagem = new Mensagem(solicitacaoRoster.toJson(), TipoDeMensagem.SOLICITACAO_ROSTER);
                    new MensagemController(this).enviaMensagem(this.pacienteParaEnvio.getPaciente().getUsuario(), mensagem);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Erro ao enviar solicitação: " + e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void adicionarPaciente(Paciente paciente) {
        try {
            SolicitacoesController.adicionarUsuario(this, medico, paciente);
            Toast.makeText(this, "Paciente adicionado! ", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao adicionar paciente: " +
                    e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void atualizarProntuario(PacienteParaEnvio pacienteParaEnvio){

        try {
            new ProntuarioController(this).atualizarProntuario(pacienteParaEnvio.getProntuarioParaEnvio(), pacienteParaEnvio.getPaciente());
            Toast.makeText(this, "Prontuário Atualizado! ", Toast.LENGTH_LONG).show();

        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao atualizar prontuário: " +
                    e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

}
