package com.ifma.appmhelp.views;

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
import com.ifma.appmhelp.daos.IDao;
import com.ifma.appmhelp.events.OnSaveModelFragment;
import com.ifma.appmhelp.factories.FactoryAlteraDadosActivity;
import com.ifma.appmhelp.factories.FactoryController;
import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.UsuarioLogado;

import java.sql.SQLException;

public class AlteraDadosActivity extends AppCompatActivity implements OnSaveModelFragment{

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

        Fragment fragment = FactoryAlteraDadosActivity.getFragment(UsuarioLogado.getInstance().getModelo());
        fragment.setArguments(this.getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container_altera_dados,fragment).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;

    }

    @Override
    public void save(IModel modelo) {
        IDao controller = FactoryController.getController(this,modelo);
        try {
            if (controller.persistir(modelo, true)) {
                Toast.makeText(this, "Dados alterados com sucesso!", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(this, controller.getMsgErro() ,Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Não foi possível atualizar os dados - " + e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }

}
