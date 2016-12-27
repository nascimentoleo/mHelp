package com.ifma.appmhelp.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.adapters.PacientesAdapter;
import com.ifma.appmhelp.models.Paciente;

import java.util.ArrayList;
import java.util.List;


public class ListPacientesFragment extends Fragment implements AdapterView.OnItemClickListener{

    private OnPatientSelectedListener mListener;
    private ListView listViewPacientes;

    public ListPacientesFragment() {
    }

    public static ListPacientesFragment newInstance(ArrayList<Paciente> listaDePacientes) {
        ListPacientesFragment fragment = new ListPacientesFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("lista_de_pacientes", listaDePacientes);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_pacientes, container, false);
    }

    private void carregaComponentes(){
        this.listViewPacientes = (ListView) getView().findViewById(R.id.list_view_pacientes);
        this.listViewPacientes.setOnItemClickListener(this);
        List<Paciente> listaDePacientes = (List<Paciente>) getArguments().getSerializable("lista_de_pacientes");
        this.listViewPacientes.setAdapter(new PacientesAdapter(getContext(),listaDePacientes));
    }

    @Override
    public void onStart() {
        super.onStart();
        this.carregaComponentes();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPatientSelectedListener) {
            mListener = (OnPatientSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mListener.onPacienteSelected((Paciente) this.listViewPacientes.getAdapter().getItem(position));
    }

    public interface OnPatientSelectedListener {
        void onPacienteSelected(Paciente paciente);
    }
}
