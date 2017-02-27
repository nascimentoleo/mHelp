package com.ifma.appmhelp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.models.Medico;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 1/3/17.
 */

public class MedicosAdapter extends RecyclerView.Adapter<MedicosAdapter.RecycleCidsViewHolder> {

    private List<MedicoOcorrencia> listaDeMedicosOcorrencia;
    private Context ctx;
    private List<Medico> medicosSelecionados;

    public MedicosAdapter(Context ctx, List<Medico> listaDeMedicos) {
        listaDeMedicosOcorrencia = new ArrayList<>();
        medicosSelecionados = new ArrayList<>();
        for(Medico medico : listaDeMedicos)
            listaDeMedicosOcorrencia.add(new MedicoOcorrencia(medico, false));

         this.ctx = ctx;
    }


    public List<Medico> getMedicosSelecionados() {
        return medicosSelecionados;
    }

    public boolean existemMedicosSelecionados(){
        return ! medicosSelecionados.isEmpty();
    }

    @Override
    public RecycleCidsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(ctx).inflate(R.layout.item_list_medicos, null);
        return new RecycleCidsViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(RecycleCidsViewHolder holder, int position) {
        MedicoOcorrencia medicoOcorrencia = listaDeMedicosOcorrencia.get(position);
        holder.chkMedicos.setText(medicoOcorrencia.medico.getUsuario().getNome());
        holder.chkMedicos.setChecked(medicoOcorrencia.selected);
    }

    @Override
    public int getItemCount() {
        return listaDeMedicosOcorrencia.size();
    }


    private void atualizaSelecaoMedico(int position, boolean checked){
        MedicoOcorrencia medicoOcorrencia = listaDeMedicosOcorrencia.get(position);
        medicoOcorrencia.setSelected(checked);

        if(checked)
            medicosSelecionados.add(medicoOcorrencia.getMedico());
        else
            medicosSelecionados.remove(medicoOcorrencia.getMedico());

    }

    public class RecycleCidsViewHolder extends RecyclerView.ViewHolder {

        private CheckBox chkMedicos;

        public RecycleCidsViewHolder(View itemView) {
            super(itemView);

            chkMedicos          = (CheckBox) itemView.findViewById(R.id.chkMedico);

            chkMedicos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox chkMedico = (CheckBox) v;
                    atualizaSelecaoMedico(getLayoutPosition(), chkMedico.isChecked());
                }
            });
        }

    }

    private class MedicoOcorrencia{
        private Medico medico;
        private boolean selected;

        public MedicoOcorrencia(Medico medico, boolean selected) {
            this.medico = medico;
            this.selected = selected;
        }

        public Medico getMedico() {
            return medico;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

}
