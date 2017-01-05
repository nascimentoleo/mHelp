package com.ifma.appmhelp.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.models.Paciente;

import java.util.List;

/**
 * Created by leo on 1/3/17.
 */

public class PacientesAdapter extends RecyclerView.Adapter<PacientesAdapter.RecycleCidsViewHolder> {

    private List<Paciente> listaDePacientes;
    private Context ctx;
    private static OnItemClickListener listenerClick;
    private static OnItemLongClickListener listenerLongClick;

    public PacientesAdapter(Context ctx, List<Paciente> listaDePacientes) {
        this.listaDePacientes = listaDePacientes;
        this.ctx = ctx;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listenerClick = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.listenerLongClick = listener;
    }

    @Override
    public RecycleCidsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(ctx).inflate(R.layout.item_list_pacientes, null);
        return new RecycleCidsViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(RecycleCidsViewHolder holder, int position) {
        Paciente paciente = listaDePacientes.get(position);
        holder.txtNomePaciente.setText(paciente.getUsuario().getNome());
        holder.txtEnderecoPaciente.setText(paciente.getEndereco());
        holder.txtTelefonePaciente.setText(paciente.getTelefone());
    }

    @Override
    public int getItemCount() {
        return listaDePacientes.size();
    }


    public class RecycleCidsViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNomePaciente;
        private TextView txtEnderecoPaciente;
        private TextView txtTelefonePaciente;
        private CardView cardViewCid;

        public RecycleCidsViewHolder(View itemView) {
            super(itemView);

            txtNomePaciente     = (TextView) itemView.findViewById(R.id.txtNomePacienteList);
            txtEnderecoPaciente = (TextView) itemView.findViewById(R.id.txtEnderecoPacienteList);
            txtTelefonePaciente = (TextView) itemView.findViewById(R.id.txtTelefonePacienteList);
            cardViewCid         = (CardView) itemView.findViewById(R.id.cardViewCid);

            cardViewCid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listenerClick != null)
                        listenerClick.onItemClick(listaDePacientes.get(getLayoutPosition()));

                }
            });

            cardViewCid.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listenerLongClick != null) {
                        listenerLongClick.onItemLongClick(listaDePacientes.get(getLayoutPosition()));
                        return true;
                    }
                    return false;
                }
            });


        }
    }

    public interface OnItemClickListener {
        void onItemClick(Paciente item);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Paciente item);
    }
}
