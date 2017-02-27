package com.ifma.appmhelp.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.models.Ocorrencia;

import java.util.List;

/**
 * Created by leo on 2/24/17.
 */

public class OcorrenciasAdapter extends RecyclerView.Adapter<OcorrenciasAdapter.RecycleCidsViewHolder> {

    private List<Ocorrencia> listaDeOcorrencias;
    private Context ctx;
    private static OnItemClickListener listenerClick;

    public OcorrenciasAdapter(Context ctx, List<Ocorrencia> listaDeOcorrencias) {
        this.listaDeOcorrencias = listaDeOcorrencias;
        this.ctx = ctx;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listenerClick = listener;
    }

    @Override
    public RecycleCidsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(ctx).inflate(R.layout.item_list_ocorrencias, null);
        return new RecycleCidsViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(RecycleCidsViewHolder holder, int position) {
        Ocorrencia ocorrencia = listaDeOcorrencias.get(position);
        holder.txtNomePaciente.setText(ocorrencia.getPaciente().getUsuario().getNome());
        holder.txtTituloOcorrencia.setText(ocorrencia.getTitulo());

        if (ocorrencia.getDataUltimaMensagem() != null)
            holder.txtDataUltimaMensagem.setText(ocorrencia.getDataUltimaMensagem().toString());
    //    holder.txtUltimaMensagem.setText(ocorrencia.getUltimaMensagem().getMsg()); Ainda vou ver como farei isso
    }

    @Override
    public int getItemCount() {
        return listaDeOcorrencias.size();
    }

    public class RecycleCidsViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNomePaciente;
        private TextView txtTituloOcorrencia;
        private TextView txtDataUltimaMensagem;
        private TextView txtUltimaMensagem;
        private CardView cardViewOcorrencia;

        public RecycleCidsViewHolder(View itemView) {
            super(itemView);

            txtNomePaciente       = (TextView) itemView.findViewById(R.id.txtNomePacienteList);
            txtTituloOcorrencia   = (TextView) itemView.findViewById(R.id.txtTituloOcorrenciaList);
            txtDataUltimaMensagem = (TextView) itemView.findViewById(R.id.txtDataUltimaMensagemList);
            txtUltimaMensagem     = (TextView) itemView.findViewById(R.id.txtUltimaMensagemList);
            cardViewOcorrencia    = (CardView) itemView.findViewById(R.id.cardViewOcorrencia);

            cardViewOcorrencia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listenerClick != null)
                        listenerClick.onItemClick(listaDeOcorrencias.get(getLayoutPosition()));

                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Ocorrencia item);
    }
}
