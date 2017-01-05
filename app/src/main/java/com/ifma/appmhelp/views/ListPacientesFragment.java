package com.ifma.appmhelp.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.adapters.PacientesAdapter;
import com.ifma.appmhelp.models.Paciente;

import java.util.ArrayList;
import java.util.List;


public class ListPacientesFragment extends Fragment implements PacientesAdapter.OnItemClickListener, PacientesAdapter.OnItemLongClickListener{

    private OnPatientSelectedListener mListener;
    private RecyclerView rViewPacientes;

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
        this.rViewPacientes  = (RecyclerView) getView().findViewById(R.id.rViewPacientes);
        this.rViewPacientes.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Paciente> listaDePacientes = (List<Paciente>) getArguments().getSerializable("lista_de_pacientes");
        PacientesAdapter adapter = new PacientesAdapter(getContext(), listaDePacientes);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);
        this.rViewPacientes.setAdapter(adapter);
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
                    + " must implement OnPatientSelectedListener");
        }
    }

    @Override
    public void onItemClick(Paciente item) {
        mListener.onPatientSelected(item);
    }

    @Override
    public void onItemLongClick(Paciente item) {
        mListener.onPatientLongSelected(item);
    }

    public interface OnPatientSelectedListener {
        void onPatientSelected(Paciente paciente);
        void onPatientLongSelected(Paciente paciente);
    }
}
