package com.ifma.appmhelp.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ifma.appmhelp.R;
import com.ifma.appmhelp.controls.MedicoPacienteController;
import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.MedicoPaciente;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.UsuarioLogado;

public class AdicionarPacienteActivity extends AppCompatActivity {

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
                    IModel paciente = new Paciente().fromJson(intentResult.getContents());
                    IModel medico   = UsuarioLogado.getInstance().getModelo();
                    IModel medicoPaciente = new MedicoPaciente(medico, paciente);
                    new MedicoPacienteController(this).persistir(medicoPaciente);
                    Toast.makeText(this, "Paciente adicionado! ", Toast.LENGTH_LONG).show();
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Erro ao adicionar paciente: " + e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
