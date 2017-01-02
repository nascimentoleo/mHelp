package com.ifma.appmhelp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.models.Cid;

import java.util.List;

/**
 * Created by leo on 12/26/16.
 */

public class CidsAdapter extends BaseAdapter {

    private List<Cid> listaDeCids;
    private Context ctx;

    public CidsAdapter(Context ctx, List<Cid> listaDeCids) {
        this.listaDeCids = listaDeCids;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return listaDeCids.size();
    }

    @Override
    public Object getItem(int position) {
        return listaDeCids.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Cid cid = listaDeCids.get(position);
        View itemLista = LayoutInflater.from(ctx).inflate(R.layout.item_list_cids, null);
        TextView txtDescricaoCid = (TextView) itemLista.findViewById(R.id.txtCidDescricaoList);
        TextView txtCodigoCid    = (TextView) itemLista.findViewById(R.id.txtCidCodigoList);
        txtDescricaoCid.setText(cid.getDescricao());
        txtCodigoCid.setText(cid.getCodigo());
        return itemLista;
    }
}
