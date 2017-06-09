package com.ifma.appmhelp.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.daos.MensagemDao;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Ocorrencia;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by leo on 2/24/17.
 */

public class OcorrenciasAdapter extends RecyclerView.Adapter<OcorrenciasAdapter.RecycleOcorrenciasViewHolder> {

    private List<Ocorrencia> listaDeOcorrencias;
    private Context ctx;
    private static OnItemClickListener listenerClick;
    private boolean exibeNomePaciente;

    public OcorrenciasAdapter(Context ctx, List<Ocorrencia> listaDeOcorrencias) {
        this.listaDeOcorrencias = listaDeOcorrencias;
        this.ctx = ctx;
        this.exibeNomePaciente = false; //Por padrao nao exibe
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listenerClick = listener;
    }

    public void setExibeNomePaciente(boolean exibeNomePaciente) {
        this.exibeNomePaciente = exibeNomePaciente;
    }

    @Override
    public RecycleOcorrenciasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(ctx).inflate(R.layout.item_list_ocorrencias, null);
        return new RecycleOcorrenciasViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(RecycleOcorrenciasViewHolder holder, int position) {
        Ocorrencia ocorrencia = listaDeOcorrencias.get(position);

        if (exibeNomePaciente){
            holder.txtNomeUsuario.setText(ocorrencia.getPaciente().getUsuario().getNome());
        }else
            holder.txtNomeUsuario.setText(ocorrencia.getMedico().getUsuario().getNome());

        holder.txtTituloOcorrencia.setText(ocorrencia.getTitulo());

        Mensagem mensagem = this.carregaUltimaMensagem(ocorrencia);

        if (mensagem != null){

            if (mensagem.getMsg() != null && !mensagem.getMsg().isEmpty())
                holder.txtUltimaMensagem.setText(mensagem.getMsg());

            else if (mensagem.getAnexo() != null)
                holder.txtUltimaMensagem.setText(mensagem.getAnexo().getTipoAnexo().toString());

            if (ocorrencia.getDataUltimaMensagem() == null)
                ocorrencia.setDataUltimaMensagem(mensagem.getData());

        }else
            holder.txtUltimaMensagem.setVisibility(View.INVISIBLE);

        if (ocorrencia.getDataUltimaMensagem() != null) {
            SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm");
            SimpleDateFormat dfData = new SimpleDateFormat("dd/MM/yyyy");

            holder.txtDataUltimaMensagem.setText(dfData.format(ocorrencia.getDataUltimaMensagem()));
            holder.txtHoraUltimaMensagem.setText(dfHora.format(ocorrencia.getDataUltimaMensagem()));
        }else {
            holder.txtDataUltimaMensagem.setVisibility(View.INVISIBLE);
            holder.txtHoraUltimaMensagem.setVisibility(View.INVISIBLE);

        }
    }

    private Mensagem carregaUltimaMensagem(Ocorrencia ocorrencia){
        try {
            return new MensagemDao(ctx).getUltimaMensagemByOcorrencia(ocorrencia);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return listaDeOcorrencias.size();
    }

    public class RecycleOcorrenciasViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNomeUsuario;
        private TextView txtTituloOcorrencia;
        private TextView txtDataUltimaMensagem;
        private TextView txtHoraUltimaMensagem;
        private TextView txtUltimaMensagem;
        private CardView cardViewOcorrencia;

        public RecycleOcorrenciasViewHolder(View itemView) {
            super(itemView);

            txtNomeUsuario        = (TextView) itemView.findViewById(R.id.txtNomeUsuarioList);
            txtTituloOcorrencia   = (TextView) itemView.findViewById(R.id.txtTituloOcorrenciaList);
            txtDataUltimaMensagem = (TextView) itemView.findViewById(R.id.txtDataUltimaMensagemList);
            txtHoraUltimaMensagem = (TextView) itemView.findViewById(R.id.txtHoraUltimaMensagemList);
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
