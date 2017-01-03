package com.ifma.appmhelp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.models.Cid;

import java.util.List;

/**
 * Created by leo on 1/3/17.
 */

public class RecycleCidsAdapter extends RecyclerView.Adapter<RecycleCidsAdapter.RecycleCidsViewHolder> {

    private List<Cid> listaDeCids;
    private Context ctx;


    public RecycleCidsAdapter(Context ctx, List<Cid> listaDeCids) {
        this.listaDeCids = listaDeCids;
        this.ctx = ctx;
    }

    @Override
    public RecycleCidsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(ctx).inflate(R.layout.item_list_cids, null);
        return new RecycleCidsViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(RecycleCidsViewHolder holder, int position) {
        Cid cid = listaDeCids.get(position);
        holder.txtCodigoCid.setText(cid.getCodigo());
        holder.txtDescricaoCid.setText(cid.getDescricao());
    }

    @Override
    public int getItemCount() {
        return listaDeCids.size();
    }

    public class RecycleCidsViewHolder extends RecyclerView.ViewHolder {

        private TextView txtDescricaoCid;
        private TextView txtCodigoCid;

        public RecycleCidsViewHolder(View itemView) {
            super(itemView);

            txtDescricaoCid = (TextView) itemView.findViewById(R.id.txtCidDescricaoList);
            txtCodigoCid    = (TextView) itemView.findViewById(R.id.txtCidCodigoList);

        }
    }
}
