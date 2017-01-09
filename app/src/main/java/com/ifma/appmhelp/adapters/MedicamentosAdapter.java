package com.ifma.appmhelp.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.models.Medicamento;

import java.util.List;

/**
 * Created by leo on 1/3/17.
 */

public class MedicamentosAdapter extends RecyclerView.Adapter<MedicamentosAdapter.RecycleMedicamentosViewHolder> {

    private List<Medicamento> listaDeMedicamentos;
    private Context ctx;
    private OnItemLongClickListener listener;

    public MedicamentosAdapter(Context ctx, List<Medicamento> listaDeMedicamentos) {
        this.listaDeMedicamentos = listaDeMedicamentos;
        this.ctx = ctx;
    }

    public List<Medicamento> getListaDeMedicamentos() {
        return listaDeMedicamentos;
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
        Medicamento medicamento = listaDeMedicamentos.get(position);
        holder.txtNomeMedicamento.setText(medicamento.getNome());
   }

    @Override
    public int getItemCount() {
        return listaDeMedicamentos.size();
    }


    public class RecycleMedicamentosViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNomeMedicamento;
        private CardView cardViewMedicamento;

        public RecycleMedicamentosViewHolder(View itemView) {
            super(itemView);

            txtNomeMedicamento   = (TextView) itemView.findViewById(R.id.txtMedicamentoNomeList);
            cardViewMedicamento  = (CardView) itemView.findViewById(R.id.cardViewMedicamento);

            cardViewMedicamento.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null)
                        listener.onItemLongClick(listaDeMedicamentos.get(getLayoutPosition()));
                    return true;
                }

            });
        }
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Medicamento item);
    }
}
