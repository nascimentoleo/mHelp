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
import com.ifma.appmhelp.controls.ProntuariosController;
import com.ifma.appmhelp.daos.PacienteDao;
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

    private Prontuario prontuario;
    private Paciente paciente;
    private Spinner spSexo;
    private Spinner spEstadoCivil;
    private Spinner spTipoSanguineo;
    private TextView txtIdade;
    private EditText edDataDeNascimento;
    private EditText edNomeDaMae;
    private EditText edNomeDoPai;
    private EditText edTelefoneResponsavel;
    private EditText edObservacoes;
    private ArrayAdapter<Sexo> adapterSexo;
    private ArrayAdapter<EstadoCivil> adapterEstadoCivil;
    private ArrayAdapter<TipoSanguineo> adapterTipoSanguineo;
    private boolean modificouProntuario; //Flag para saber se houve alterações
    private boolean permiteEditar; //Flag para saber se o prontuário poderá ser editado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prontuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        permiteEditar = getIntent().getBooleanExtra(GenericBundleKeys.EDITAR_PRONTUARIO.toString(), false);

        //Somente médicos estão autorizados a editar um prontuário
        if(permiteEditar){
            paciente = (Paciente) getIntent().getSerializableExtra(GenericBundleKeys.PACIENTE.toString());
            prontuario = paciente.getProntuario();
        }
        else
            prontuario = (Prontuario) getIntent().getSerializableExtra(GenericBundleKeys.PRONTUARIO.toString());

        this.inicializaComponentes();
        this.carregaAdapters();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.carregaProntuario();
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
                it.putExtra(GenericBundleKeys.PRONTUARIO.toString(),this.prontuario);
                it.putExtra(GenericBundleKeys.EDITAR_PRONTUARIO.toString(),permiteEditar);
                startActivityForResult(it, RESULT_FIRST_USER);
                return true;
            case R.id.action_medicamentos :
                it = new Intent(this,MedicamentoActivity.class);
                it.putExtra(GenericBundleKeys.PRONTUARIO.toString(),this.prontuario);
                it.putExtra(GenericBundleKeys.EDITAR_PRONTUARIO.toString(),permiteEditar);
                startActivityForResult(it, RESULT_FIRST_USER);
                return true;
        }
        return false;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            this.prontuario = (Prontuario) data.getSerializableExtra(GenericBundleKeys.PRONTUARIO.toString());

            if (!modificouProntuario)
                modificouProntuario = data.getBooleanExtra(GenericBundleKeys.MODIFICOU_PRONTUARIO.toString(), false);
        }
    }

    private void inicializaComponentes(){
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

        this.modificouProntuario = false;

    }

    @Override
    protected void onDestroy() {
        if (permiteEditar && modificouProntuario){
            ProntuariosController controller = new ProntuariosController(this);
            if (controller.enviarProntuario(this.prontuario, this.paciente.getUsuario()))
                Toast.makeText(this, "Prontuário enviado" , Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Erro ao enviar prontuário: " + controller.getMsgErro(), Toast.LENGTH_SHORT).show();

        }

        super.onDestroy();
    }

    private void carregaProntuario(){
        if (this.prontuario != null) {
            if (prontuario.getIdade() > 0)
                txtIdade.setText(Integer.toString(prontuario.getIdade()) + " anos");
            else
                txtIdade.setText("");

            edDataDeNascimento.setText("");
            if (prontuario.getDataDeNascimento() != null)
                edDataDeNascimento.setText(prontuario.getDataDeNascimentoString());

            edNomeDaMae.setText(prontuario.getNomeDaMae());
            edNomeDoPai.setText(prontuario.getNomeDoPai());
            edTelefoneResponsavel.setText(prontuario.getTelefoneDoResponsavel());
            edObservacoes.setText(prontuario.getObservacoes());

            int spSexoPosition          = adapterSexo.getPosition(prontuario.getSexo());
            int spEstadoCivilPosition   = adapterEstadoCivil.getPosition(prontuario.getEstadoCivil());
            int spTipoSanguineoPosition = adapterTipoSanguineo.getPosition(prontuario.getTipoSanguineo());
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
            this.preencherProntuario(prontuario);
            try {
                new PacienteDao(this).persistir(paciente, true);
                this.modificouProntuario = true;
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
