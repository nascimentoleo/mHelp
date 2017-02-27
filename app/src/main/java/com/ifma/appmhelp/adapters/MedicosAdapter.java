package com.ifma.appmhelp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.models.Medico;

import java.util.List;

/**
 * Created by leo on 1/3/17.
 */

public class MedicosAdapter extends BaseAdapter{

    private Context ctx;
    private List<Medico> listaDeMedicos;

    public MedicosAdapter(Context ctx, List<Medico> listaDeMedicos) {
        this.listaDeMedicos = listaDeMedicos;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return listaDeMedicos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Medico medico = this.listaDeMedicos.get(position);
        View itemLista = LayoutInflater.from(ctx).inflate(
                R.layout.item_list_medicos, null);

        TextView txtMedicoOcorrencia  = (TextView) itemLista.findViewById(R.id.txtMedicoOcorrencia);
        txtMedicoOcorrencia.setText(medico.getUsuario().getNome());
        return itemLista;
    }

}
