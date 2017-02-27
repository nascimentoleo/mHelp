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
import com.ifma.appmhelp.adapters.OcorrenciasAdapter;
import com.ifma.appmhelp.models.Ocorrencia;

import java.util.ArrayList;
import java.util.List;


public class ListOcorrenciasFragment extends Fragment  implements OcorrenciasAdapter.OnItemClickListener{

    private OnOcorrenciaSelectedListener mListener;
    private RecyclerView rViewOcorrencias;

    public ListOcorrenciasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        this.carregaComponentes();
    }

    private void carregaComponentes(){
        this.rViewOcorrencias  = (RecyclerView) getView().findViewById(R.id.rViewOcorrencias);
        this.rViewOcorrencias.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Ocorrencia> listaDeOcorrencias = (List<Ocorrencia>) getArguments().getSerializable("lista_de_ocorrencias");
        OcorrenciasAdapter adapter = new OcorrenciasAdapter(getContext(), listaDeOcorrencias);
        adapter.setOnItemClickListener(this);
        this.rViewOcorrencias.setAdapter(adapter);
    }

    public static ListOcorrenciasFragment newInstance(ArrayList<Ocorrencia> listaDeOcorrencias) {
        ListOcorrenciasFragment fragment = new ListOcorrenciasFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("lista_de_ocorrencias", listaDeOcorrencias);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_ocorrencias, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOcorrenciaSelectedListener) {
            mListener = (OnOcorrenciaSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnOcorrenciaSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(Ocorrencia item) {
        mListener.OnOcorrenciaSelected(item);
    }

    public interface OnOcorrenciaSelectedListener {
        void OnOcorrenciaSelected(Ocorrencia ocorrencia);
    }
}
