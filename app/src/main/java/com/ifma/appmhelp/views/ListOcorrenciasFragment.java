package com.ifma.appmhelp.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.adapters.OcorrenciasAdapter;
import com.ifma.appmhelp.controls.OcorrenciaPagination;
import com.ifma.appmhelp.lib.EndlessRecyclerViewScrollListener;
import com.ifma.appmhelp.models.Ocorrencia;

import java.sql.SQLException;
import java.util.List;


public class ListOcorrenciasFragment extends Fragment implements OcorrenciasAdapter.OnItemClickListener{

    private OnOcorrenciaSelectedListener mListener;
    private RecyclerView rViewOcorrencias;
    private List<Ocorrencia> listaDeOcorrencias;
    private OcorrenciaPagination ocorrenciaPagination;
    private boolean usuarioEhMedico;

    public ListOcorrenciasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        this.carregaComponentes();
        this.inicializaAdapter();
    }



    private void carregaComponentes(){
        this.ocorrenciaPagination = (OcorrenciaPagination) getArguments().getSerializable("ocorrencia_pagination");
        this.usuarioEhMedico = getArguments().getBoolean("usuario_medico");
        this.rViewOcorrencias  = (RecyclerView) getView().findViewById(R.id.rViewOcorrencias);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        this.rViewOcorrencias.setLayoutManager(linearLayoutManager);

        this.rViewOcorrencias.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(totalItemsCount);

            }
        });
    }

    private void inicializaAdapter() {
        try {
            listaDeOcorrencias = ocorrenciaPagination.getListaDeOcorrencias(getContext(), 0);//Pega a partir do primeiro
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(getContext(),
                    "Erro ao carregar ocorrências: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        OcorrenciasAdapter adapter = new OcorrenciasAdapter(getContext(), listaDeOcorrencias);
        adapter.setExibeNomePaciente(usuarioEhMedico);
        adapter.setOnItemClickListener(this);
        this.rViewOcorrencias.setAdapter(adapter);
    }

    private void loadNextDataFromApi(int totalItemsCount) {
        try {
            listaDeOcorrencias.addAll(ocorrenciaPagination.getListaDeOcorrencias(getContext(), totalItemsCount));
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(getContext(),
                    "Erro ao carregar ocorrências: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
         rViewOcorrencias.getAdapter().notifyDataSetChanged();
    }

    public static ListOcorrenciasFragment newInstance(OcorrenciaPagination ocorrenciaPagination, boolean usuarioEhMedico) {
        ListOcorrenciasFragment fragment = new ListOcorrenciasFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("ocorrencia_pagination", ocorrenciaPagination);
        bundle.putBoolean("usuario_medico", usuarioEhMedico);
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
