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
import com.ifma.appmhelp.models.Medico;
import com.ifma.appmhelp.models.UsuarioLogado;

public class AlteraMedicoFragment extends Fragment implements View.OnClickListener{

    private EditText edNome;
    private EditText edCrm;
    private EditText edEmail;
    private Medico medico;
    private Button btnAlterar;
    private OnSaveModelFragment activityMain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.medico = (Medico) UsuarioLogado.getInstance().getModelo();
        return inflater.inflate(R.layout.fragment_altera_medico, container, false);
    }

    @Override
    public void onStart() {
        // Fragment só tem acesso a view após o onCreteView,
        // impossibilitando de registrar os componentes no onCreate
        this.registraComponentes();
        this.carregarValores();
        super.onStart();
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
        this.edNome       = (EditText) getView().findViewById(R.id.edNomeMedico);
        this.edCrm        = (EditText) getView().findViewById(R.id.edCRM);
        this.edEmail      = (EditText) getView().findViewById(R.id.edEmailMedico);
        this.btnAlterar = (Button) getView().findViewById(R.id.btnAlterarMedico);
        btnAlterar.setOnClickListener(this);

    }

    private void carregarValores() {
        this.edNome.setText(this.medico.getUsuario().getNome());
        this.edCrm.setText(this.medico.getCrm());
        this.edEmail.setText(this.medico.getUsuario().getEmail());
    }

    @Override
    public void onClick(View v) {
        this.medico.getUsuario().setNome(this.edNome.getText().toString().trim());
        this.medico.getUsuario().setEmail(this.edEmail.getText().toString().trim());
        this.medico.setCrm(this.edCrm.getText().toString().trim());

        activityMain.save(this.medico);
    }
}
