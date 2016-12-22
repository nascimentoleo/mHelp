package com.ifma.appmhelp.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.events.OnSaveModelFragment;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.UsuarioLogado;


public class AlteraPacienteFragment extends Fragment implements View.OnClickListener {

    private EditText edNome;
    private EditText edEndereco;
    private EditText edEmail;
    private EditText edTelefone;
    private Button btnAlterar;
    private OnSaveModelFragment activityMain;
    private Paciente paciente;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.paciente = (Paciente) UsuarioLogado.getInstance().getModelo();
        return inflater.inflate(R.layout.fragment_altera_paciente, container, false);
    }

    @Override
    public void onStart() {
        // Fragment só tem acesso a view após o onCreteView,
        // impossibilitando de registrar os componentes no onCreate
        this.registraComponentes();
        this.carregarValores();
        super.onStart();
    }

    private void carregarValores() {
        this.edNome.setText(this.paciente.getUsuario().getNome());
        this.edEmail.setText(this.paciente.getUsuario().getEmail());
        this.edEndereco.setText(this.paciente.getEndereco());
        this.edTelefone.setText(this.paciente.getTelefone());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            activityMain = (OnSaveModelFragment) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnSaveModelFragment");
        };
    }

    private void registraComponentes(){
        this.edNome       = (EditText) getView().findViewById(R.id.edNomePaciente);
        this.edEndereco   = (EditText) getView().findViewById(R.id.edEnderecoPaciente);
        this.edEmail      = (EditText) getView().findViewById(R.id.edEmailPaciente);
        this.edTelefone   = (EditText) getView().findViewById(R.id.edTelefonePaciente);
        this.btnAlterar   = (Button) getView().findViewById(R.id.btnAlterarPaciente);
        btnAlterar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        this.paciente.getUsuario().setNome(this.edNome.getText().toString().trim());
        this.paciente.getUsuario().setEmail(this.edEmail.getText().toString().trim());
        this.paciente.setEndereco(this.edEndereco.getText().toString().trim());
        this.paciente.setTelefone(this.edTelefone.getText().toString().trim());

        activityMain.save(this.paciente);

    }
}
