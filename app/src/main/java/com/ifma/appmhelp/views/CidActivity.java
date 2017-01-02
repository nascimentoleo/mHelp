package com.ifma.appmhelp.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.enums.GenericBundleKeys;
import com.ifma.appmhelp.models.Paciente;

public class CidActivity extends AppCompatActivity {

    private Paciente paciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.paciente = (Paciente) getIntent().getSerializableExtra(GenericBundleKeys.PACIENTE.toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent returnIntent = new Intent();
                returnIntent.putExtra(GenericBundleKeys.PACIENTE.toString(),paciente);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
                return true;
        }
        return false;
    }
}
