package com.ifma.appmhelp.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.controls.IController;
import com.ifma.appmhelp.enums.BundleKeys;
import com.ifma.appmhelp.events.OnSaveModelFragment;
import com.ifma.appmhelp.factories.FactoryAlteraDadosActivity;
import com.ifma.appmhelp.factories.FactoryController;
import com.ifma.appmhelp.models.IModel;

import java.sql.SQLException;

public class AlteraDadosActivity extends AppCompatActivity implements OnSaveModelFragment{

    private IModel usuarioLogado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altera_dados);
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

        usuarioLogado = (IModel) this.getIntent().getExtras().getSerializable(BundleKeys.USUARIO_LOGADO.getValue());
        Fragment fragment = FactoryAlteraDadosActivity.getFragment(usuarioLogado);
        fragment.setArguments(this.getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container_altera_dados,fragment).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent it = new Intent();
                it.putExtra(BundleKeys.USUARIO_LOGADO.getValue(),this.usuarioLogado);
                setResult(RESULT_OK, it);
                finish();
                return true;
        }
        return false;

    }

    @Override
    public void save(IModel modelo) {
        IController controller = FactoryController.getController(this,modelo);
        try {
            if (controller.persistir(modelo)) {
                Toast.makeText(this, "Dados alterados com sucesso!", Toast.LENGTH_SHORT).show();
                this.usuarioLogado = modelo;
            }else
                Toast.makeText(this, controller.getMsgErro() ,Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Não foi possível atualizar os dados - " + e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }
}
