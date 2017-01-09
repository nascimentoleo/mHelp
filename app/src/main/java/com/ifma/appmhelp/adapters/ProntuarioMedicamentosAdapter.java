package com.ifma.appmhelp.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.models.ProntuarioMedicamento;

import java.util.List;

/**
 * Created by leo on 1/3/17.
 */

public class ProntuarioMedicamentosAdapter extends RecyclerView.Adapter<ProntuarioMedicamentosAdapter.RecycleMedicamentosViewHolder> {

    private List<ProntuarioMedicamento> listaDeProntuarioMedicamentos;
    private Context ctx;
    private OnItemLongClickListener listener;

    public ProntuarioMedicamentosAdapter(Context ctx, List<ProntuarioMedicamento> listaDeProntuarioMedicamentos) {
        this.listaDeProntuarioMedicamentos = listaDeProntuarioMedicamentos;
        this.ctx = ctx;
    }

    public List<ProntuarioMedicamento> getListaDeProntuarioMedicamentos() {
        return listaDeProntuarioMedicamentos;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecycleMedicamentosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(ctx).inflate(R.layout.item_list_medicamentos, null);
        return new RecycleMedicamentosViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(RecycleMedicamentosViewHolder holder, int position) {
        ProntuarioMedicamento prontuarioMedicamento = listaDeProntuarioMedicamentos.get(position);
        holder.txtNomeMedicamento.setText(prontuarioMedicamento.getMedicamento().getNome());
        holder.txtDosesMedicamento.setText(prontuarioMedicamento.getDoses());
   }

    @Override
    public int getItemCount() {
        return listaDeProntuarioMedicamentos.size();
    }


    public class RecycleMedicamentosViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNomeMedicamento;
        private TextView txtDosesMedicamento;
        private CardView cardViewMedicamento;

        public RecycleMedicamentosViewHolder(View itemView) {
            super(itemView);

            txtNomeMedicamento   = (TextView) itemView.findViewById(R.id.txtMedicamentoNomeList);
            txtDosesMedicamento  = (TextView) itemView.findViewById(R.id.txtMedicamentoDosesList);
            cardViewMedicamento  = (CardView) itemView.findViewById(R.id.cardViewMedicamento);

            cardViewMedicamento.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null)
                        listener.onItemLongClick(listaDeProntuarioMedicamentos.get(getLayoutPosition()));
                    return true;
                }

            });
        }
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(ProntuarioMedicamento item);
    }
}
