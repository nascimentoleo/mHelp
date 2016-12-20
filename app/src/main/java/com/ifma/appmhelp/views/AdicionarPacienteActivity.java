package com.ifma.appmhelp.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ifma.appmhelp.R;
import com.ifma.appmhelp.controls.RosterXMPPController;
import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.MedicoPaciente;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.Usuario;
import com.ifma.appmhelp.models.UsuarioLogado;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Presence;

public class AdicionarPacienteActivity extends AppCompatActivity {

    private IModel paciente;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getBooleanExtra("aceitou_solicitacao", false)){
                IModel medico   = UsuarioLogado.getInstance().getModelo();
                IModel medicoPaciente = new MedicoPaciente(medico, paciente);
                try {
                    //new MedicoPacienteController(AdicionarPacienteActivity.this).persistir(medicoPaciente);
                    Toast.makeText(AdicionarPacienteActivity.this, "Paciente adicionado! ", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(AdicionarPacienteActivity.this, "Erro ao adicionar paciente: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }


            }else {
                Toast.makeText(AdicionarPacienteActivity.this, "Paciente recusou a solicitação", Toast.LENGTH_SHORT).show();
                RosterXMPPController roster = new RosterXMPPController(AdicionarPacienteActivity.this);
                Usuario usuario = ((Paciente) paciente).getUsuario();
                try {
                    roster.sendPresence(usuario,Presence.Type.unsubscribe);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_paciente);
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
        this.iniciaQrScan();
    }

    private void iniciaQrScan(){
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Posicione a camera para escanear o código do paciente");
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(intentResult != null) {
            if(intentResult.getContents() != null) {
                try {
                    paciente = new Paciente().fromJson(intentResult.getContents());
                    new RosterXMPPController(this).sendPresence(((Paciente) paciente).getUsuario(), Presence.Type.subscribe);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Erro ao enviar solicitação: " + e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
