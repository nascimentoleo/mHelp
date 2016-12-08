package com.ifma.appmhelp.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.controls.Login;
import com.ifma.appmhelp.enums.BundleKeys;
import com.ifma.appmhelp.factories.FactoryChat;
import com.ifma.appmhelp.models.ConexaoXMPP;
import com.ifma.appmhelp.models.Medico;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

public class MedicoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView txtMsg;
    private EditText edMensagem;
    private Chat chat;
    private Medico medico;
    private ChatManager chatManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medico);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        this.medico = (Medico) getIntent().getExtras().getSerializable(BundleKeys.USUARIO_LOGADO.getValue());
        registrarComponentes();
        this.iniciaChat();
    }

    private void registrarComponentes() {
        txtMsg = (TextView) findViewById(R.id.txtMsg);
        edMensagem = (EditText) findViewById(R.id.edMensagem);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.medico, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch(item.getItemId()){
            case R.id.nav_alterar_dados :
                Bundle bundle = new Bundle();
                bundle.putSerializable(BundleKeys.USUARIO_LOGADO.getValue(), this.medico);
                startActivityForResult(new Intent(this, AlteraDadosActivity.class).putExtras(bundle), RESULT_FIRST_USER);
                break;
            case R.id.nav_logoff_medico:
                try {
                    new Login(this).realizaLogoff(this.medico.getUsuario());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(this,MainActivity.class));
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void iniciaChat(){
        if (ConexaoXMPP.getInstance().conexaoEstaAtiva()){
            chatManager = ChatManager.getInstanceFor(ConexaoXMPP.getInstance().getConexao());
            chatManager.addChatListener(new MyChatManagerListener());
        }
    }

    public void enviarMensagem(View v){
        //if(this.chat != null){
            try {
                this.chat = FactoryChat.novoChat("medico", ConexaoXMPP.getInstance().getConexao());
                this.chat.addMessageListener(new MyMessageListener());
                this.chat.sendMessage(this.edMensagem.getText().toString());
            } catch (SmackException.NotConnectedException e) {
                Toast.makeText(MedicoActivity.this, "Não foi possível enviar a mensagem", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

    }

    class MyMessageListener implements ChatMessageListener{
        @Override
        public void processMessage(Chat chat, Message message) {
            txtMsg.setText(message.getBody());
        }

    }

    class MyChatManagerListener implements ChatManagerListener{
        @Override
        public void chatCreated(Chat chat, boolean createdLocally) {
                if (!createdLocally)
                    chat.addMessageListener(new MyMessageListener());;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK)
            this.medico = (Medico) data.getExtras().getSerializable(BundleKeys.USUARIO_LOGADO.getValue());
        super.onActivityResult(requestCode, resultCode, data);
    }
}
