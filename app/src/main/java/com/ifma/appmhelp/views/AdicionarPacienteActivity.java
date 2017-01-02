package com.ifma.appmhelp.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ifma.appmhelp.R;
import com.ifma.appmhelp.controls.MensagemController;
import com.ifma.appmhelp.controls.SolicitacoesController;
import com.ifma.appmhelp.enums.IntentType;
import com.ifma.appmhelp.enums.SolicitacaoBundleKeys;
import com.ifma.appmhelp.enums.StatusSolicitacaoRoster;
import com.ifma.appmhelp.enums.TipoDeMensagem;
import com.ifma.appmhelp.models.Medico;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.SolicitacaoRoster;
import com.ifma.appmhelp.models.UsuarioLogado;

public class AdicionarPacienteActivity extends AppCompatActivity {

    private Paciente paciente;
    private Medico medico;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra(SolicitacaoBundleKeys.FINALIZOU.toString(), false)) {
                if (intent.getBooleanExtra(SolicitacaoBundleKeys.ACEITOU_SOLICITACAO.toString(), false)) {
                    adicionarPaciente(paciente);
                }else
                    Toast.makeText(AdicionarPacienteActivity.this,"Paciente recusou a solicitação",Toast.LENGTH_SHORT).show();
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
                    this.paciente = new Paciente().fromJson(intentResult.getContents());
                    SolicitacaoRoster solicitacaoRoster = new SolicitacaoRoster(medico.getUsuario().clone(), StatusSolicitacaoRoster.ENVIADA);
                    Mensagem mensagem = new Mensagem(solicitacaoRoster.toJson(), TipoDeMensagem.SOLICITACAO_ROSTER);
                    MensagemController.enviaMensagem(this.paciente.getUsuario(), mensagem);
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
        finish();
    }

}
