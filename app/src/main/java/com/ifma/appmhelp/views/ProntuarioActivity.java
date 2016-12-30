package com.ifma.appmhelp.views;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.daos.PacientesDao;
import com.ifma.appmhelp.enums.EstadoCivil;
import com.ifma.appmhelp.enums.GenericBundleKeys;
import com.ifma.appmhelp.enums.Sexo;
import com.ifma.appmhelp.enums.TipoSanguineo;
import com.ifma.appmhelp.lib.KeyboardLib;
import com.ifma.appmhelp.lib.Mask;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.Prontuario;

import java.sql.Date;
import java.sql.SQLException;

public class ProntuarioActivity extends AppCompatActivity {

    private Paciente paciente;
    private Spinner spSexo;
    private Spinner spEstadoCivil;
    private Spinner spTipoSanguineo;
    private TextView txtNomePaciente;
    private TextView txtEndereco;
    private TextView txtTelefonePaciente;
    private EditText edIdade;
    private EditText edDataDeNascimento;
    private EditText edNomeDaMae;
    private EditText edNomeDoPai;
    private EditText edTelefoneResponsavel;
    private EditText edObservacoes;
    private ArrayAdapter<Sexo> adapterSexo;
    private ArrayAdapter<EstadoCivil> adapterEstadoCivil;
    private ArrayAdapter<TipoSanguineo> adapterTipoSanguineo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prontuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.inicializaComponentes();
        this.carregaAdapters();
        this.carregaProntuarioDoPaciente();

    }

    private void inicializaComponentes(){
        txtNomePaciente       = (TextView) findViewById(R.id.txtNomePacienteProntuario);
        txtEndereco           = (TextView) findViewById(R.id.txtEnderecoProntuario);
        txtTelefonePaciente   = (TextView) findViewById(R.id.txtTelefoneProntuario);
        spSexo                = (Spinner) findViewById(R.id.spSexoProntuario);
        spEstadoCivil         = (Spinner) findViewById(R.id.spEstadoCivilProntuario);
        spTipoSanguineo       = (Spinner) findViewById(R.id.spTipoSanguineoProntuario);
        edIdade               = (EditText) findViewById(R.id.edIdadeProntuario);
        edDataDeNascimento    = (EditText) findViewById(R.id.edDataDeNascimentoProntuario);
        edDataDeNascimento.addTextChangedListener(Mask.insert("##/##/####", edDataDeNascimento));
        edNomeDaMae           = (EditText) findViewById(R.id.edNomeDaMaeProntuario);
        edNomeDoPai           = (EditText) findViewById(R.id.edNomeDoPaiProntuario);
        edTelefoneResponsavel = (EditText) findViewById(R.id.edTelefoneResponsavelProntuario);
        edObservacoes         = (EditText) findViewById(R.id.edObservacoesProntuario);
    }

    private void carregaProntuarioDoPaciente(){
        paciente = (Paciente) getIntent().getSerializableExtra(GenericBundleKeys.PACIENTE.toString());
        if (paciente != null) {
            if(!paciente.getUsuario().getNome().equals(""))
                txtNomePaciente.setText(txtNomePaciente.getText() + ": " + paciente.getUsuario().getNome());
            if(!paciente.getEndereco().equals(""))
                txtEndereco.setText(txtEndereco.getText() + ": " + paciente.getEndereco());
            if(!paciente.getTelefone().equals(""))
                txtTelefonePaciente.setText(txtTelefonePaciente.getText() + ": " +paciente.getTelefone());

            if (paciente.getProntuario() != null){

                if (paciente.getProntuario().getIdade() > 0)
                    edIdade.setText(Integer.toString(paciente.getProntuario().getIdade()));

                if (paciente.getProntuario().getDataDeNascimento() != null)
                    edDataDeNascimento.setText(paciente.getProntuario().getDataDeNascimento().toString());

                edNomeDaMae.setText(paciente.getProntuario().getNomeDaMae());
                edNomeDoPai.setText(paciente.getProntuario().getNomeDoPai());
                edTelefoneResponsavel.setText(paciente.getProntuario().getTelefoneDoResponsavel());
                edObservacoes.setText(paciente.getProntuario().getObservacoes());

                int spSexoPosition          = adapterSexo.getPosition(paciente.getProntuario().getSexo());
                int spEstadoCivilPosition   = adapterEstadoCivil.getPosition(paciente.getProntuario().getEstadoCivil());
                int spTipoSanguineoPosition = adapterTipoSanguineo.getPosition(paciente.getProntuario().getTipoSanguineo());
                spSexo.setSelection(spSexoPosition);
                spEstadoCivil.setSelection(spEstadoCivilPosition);
                spTipoSanguineo.setSelection(spTipoSanguineoPosition);

            }else
                paciente.setProntuario(new Prontuario());

        }else
            Snackbar.make(findViewById(android.R.id.content), "Não foi possível carregar os dados do paciente", Snackbar.LENGTH_LONG).show();
    }

    private void carregaAdapters(){
        adapterSexo          = new ArrayAdapter<Sexo>(this, android.R.layout.simple_spinner_item, Sexo.values());
        adapterEstadoCivil   = new ArrayAdapter<EstadoCivil>(this, android.R.layout.simple_spinner_item, EstadoCivil.values());
        adapterTipoSanguineo = new ArrayAdapter<TipoSanguineo>(this, android.R.layout.simple_spinner_item, TipoSanguineo.values());

        spSexo.setAdapter(adapterSexo);
        spEstadoCivil.setAdapter(adapterEstadoCivil);
        spTipoSanguineo.setAdapter(adapterTipoSanguineo);

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

    public void alterarProntuario(View v){
        KeyboardLib.fecharTeclado(this);
        if (this.prontuarioEhValido()){
            this.preencherProntuario(paciente.getProntuario());
            try {
                new PacientesDao(this).persistir(paciente, true);
                Snackbar.make(findViewById(android.R.id.content), "Prontuário alterado", Snackbar.LENGTH_LONG).show();
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(this,"Erro ao alterar prontuário: " + e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void preencherProntuario(Prontuario prontuario){
        prontuario.setIdade(Integer.parseInt(edIdade.getText().toString().trim()));
        prontuario.setNomeDaMae(edNomeDaMae.getText().toString().trim());
        prontuario.setNomeDoPai(edNomeDoPai.getText().toString().trim());
        prontuario.setDataDeNascimento(Date.valueOf(edDataDeNascimento.getText().toString().trim()));
        prontuario.setTelefoneDoResponsavel(edTelefoneResponsavel.getText().toString().trim());
        prontuario.setObservacoes(edObservacoes.getText().toString().trim());
        prontuario.setSexo((Sexo) spSexo.getSelectedItem());
        prontuario.setEstadoCivil((EstadoCivil) spEstadoCivil.getSelectedItem());
        prontuario.setTipoSanguineo((TipoSanguineo) spTipoSanguineo.getSelectedItem());

    }

    private boolean prontuarioEhValido(){
        if (edIdade.getText().toString().trim().equals("")) {
            Snackbar.make(findViewById(android.R.id.content), "Preencha a idade", Snackbar.LENGTH_LONG).show();
            edIdade.setFocusable(true);
            return false;
        }
        else if (edDataDeNascimento.getText().toString().trim().equals("")) {
            Snackbar.make(findViewById(android.R.id.content), "Preencha a Data de Nascimento", Snackbar.LENGTH_LONG).show();
            edDataDeNascimento.setFocusable(true);
            return false;
        }

        return true;
    }

}
