package com.ifma.appmhelp.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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
import com.ifma.appmhelp.lib.DataLib;
import com.ifma.appmhelp.lib.KeyboardLib;
import com.ifma.appmhelp.lib.Mask;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.Prontuario;

import java.sql.SQLException;

public class ProntuarioActivity extends AppCompatActivity {

    private Paciente paciente;
    private Spinner spSexo;
    private Spinner spEstadoCivil;
    private Spinner spTipoSanguineo;
    /*private TextView txtNomePaciente;
    private TextView txtEndereco;
    private TextView txtTelefonePaciente; */
    private TextView txtIdade;
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.carregaProntuarioDoPaciente();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.prontuario, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent it;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_cid :
                it = new Intent(this,CidActivity.class);
                it.putExtra(GenericBundleKeys.PACIENTE.toString(),this.paciente);
                startActivityForResult(it, RESULT_FIRST_USER);
                return true;
            case R.id.action_medicamentos :
                it = new Intent(this,MedicamentoActivity.class);
                it.putExtra(GenericBundleKeys.PACIENTE.toString(),this.paciente);
                startActivityForResult(it, RESULT_FIRST_USER);
                return true;
        }
        return false;

    }

    private void inicializaComponentes(){
        /*txtNomePaciente       = (TextView) findViewById(R.id.txtNomePacienteProntuario);
        txtEndereco           = (TextView) findViewById(R.id.txtEnderecoProntuario);
        txtTelefonePaciente   = (TextView) findViewById(R.id.txtTelefoneProntuario); */
        spSexo                = (Spinner) findViewById(R.id.spSexoProntuario);
        spEstadoCivil         = (Spinner) findViewById(R.id.spEstadoCivilProntuario);
        spTipoSanguineo       = (Spinner) findViewById(R.id.spTipoSanguineoProntuario);
        txtIdade               = (TextView) findViewById(R.id.txtIdadeProntuario);
        edNomeDaMae           = (EditText) findViewById(R.id.edNomeDaMaeProntuario);
        edNomeDoPai           = (EditText) findViewById(R.id.edNomeDoPaiProntuario);
        edTelefoneResponsavel = (EditText) findViewById(R.id.edTelefoneResponsavelProntuario);
        edObservacoes         = (EditText) findViewById(R.id.edObservacoesProntuario);

        edDataDeNascimento    = (EditText) findViewById(R.id.edDataDeNascimentoProntuario);
        edDataDeNascimento.addTextChangedListener(Mask.insert("##/##/####", edDataDeNascimento));

        edDataDeNascimento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                preencherIdade();
            }
        });

    }

    private void carregaProntuarioDoPaciente(){
        paciente = (Paciente) getIntent().getSerializableExtra(GenericBundleKeys.PACIENTE.toString());
        if (paciente != null) {
           /* if(paciente.getUsuario().getNome() != null)
                txtNomePaciente.setText("Nome do Paciente: " + paciente.getUsuario().getNome());
            if(paciente.getEndereco() != null)
                txtEndereco.setText("Endereço: " + paciente.getEndereco());
            if(paciente.getTelefone() != null)
                txtTelefonePaciente.setText("Telefone: " +paciente.getTelefone());
            */

                if (paciente.getProntuario().getIdade() > 0)
                    txtIdade.setText(Integer.toString(paciente.getProntuario().getIdade()) + " anos");
                else
                    txtIdade.setText("");

                edDataDeNascimento.setText("");
                if (paciente.getProntuario().getDataDeNascimento() != null)
                    edDataDeNascimento.setText(paciente.getProntuario().getDataDeNascimentoString());

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
        int idade = Integer.parseInt(txtIdade.getText().toString().trim().replaceAll("\\D", ""));
        prontuario.setIdade(idade);
        prontuario.setNomeDaMae(edNomeDaMae.getText().toString().trim());
        prontuario.setNomeDoPai(edNomeDoPai.getText().toString().trim());
        prontuario.setDataDeNascimento(DataLib.converterData(edDataDeNascimento.getText().toString().trim()));
        prontuario.setTelefoneDoResponsavel(edTelefoneResponsavel.getText().toString().trim());
        prontuario.setObservacoes(edObservacoes.getText().toString().trim());
        prontuario.setSexo((Sexo) spSexo.getSelectedItem());
        prontuario.setEstadoCivil((EstadoCivil) spEstadoCivil.getSelectedItem());
        prontuario.setTipoSanguineo((TipoSanguineo) spTipoSanguineo.getSelectedItem());

    }

    private boolean prontuarioEhValido(){
       if (edDataDeNascimento.getText().toString().trim().equals("")) {
            Snackbar.make(findViewById(android.R.id.content), "Preencha a Data de Nascimento", Snackbar.LENGTH_LONG).show();
            edDataDeNascimento.setFocusable(true);
            return false;
        }

        return true;
    }

    private void preencherIdade() {
        if (!edDataDeNascimento.getText().toString().equals(""))
            if (dataEhValida(edDataDeNascimento.getText().toString()))
                txtIdade.setText(DataLib.calulaIdade(DataLib.converterData(edDataDeNascimento.getText().toString())) + " anos");
            else {
                Toast.makeText(this,"Data de Nascimento inválida", Toast.LENGTH_SHORT).show();
                edDataDeNascimento.setText("");
            }
    }

    private boolean dataEhValida(String data){
        return DataLib.validarDataDeNascimento(data);
    }

}
