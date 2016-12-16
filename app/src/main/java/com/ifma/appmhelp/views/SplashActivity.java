package com.ifma.appmhelp.views;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.models.ConexaoXMPP;
import com.ifma.appmhelp.services.ConexaoXMPPService;
import com.race604.drawable.wave.WaveDrawable;

public class SplashActivity extends Activity {

    private ImageView imgLogo;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getBooleanExtra("finalizou_conexao", false))
                iniciaAplicacao();
            else {
                Toast.makeText(SplashActivity.this, "Não foi possível conectar ao servidor, tente novamente mais tarde", Toast.LENGTH_SHORT).show();
                desconectar();
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("conectar"));
        this.inicializaComponentes();
        this.conectar();
    }

    private void inicializaComponentes(){
        this.imgLogo = (ImageView) findViewById(R.id.imgLogo);
        WaveDrawable mWaveDrawable = new WaveDrawable(this,R.drawable.ic_launcher);
        mWaveDrawable.setIndeterminate(true);
        mWaveDrawable.setWaveSpeed(20);
        imgLogo.setImageDrawable(mWaveDrawable);

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void desconectar(){
        stopService(new Intent(this, ConexaoXMPPService.class));
    }

    private void conectar() {
        if(!ConexaoXMPP.getInstance().conexaoEstaAtiva())
            startService(new Intent(this, ConexaoXMPPService.class));

    }

    private void iniciaAplicacao(){
        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(1000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }
}
