package com.ifma.appmhelp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.models.Paciente;

import java.util.List;

/**
 * Created by leo on 12/26/16.
 */

public class PacientesAdapter extends BaseAdapter {

    private List<Paciente> listaDePacientes;
    private Context ctx;

    public PacientesAdapter(Context ctx, List<Paciente> listaDePacientes) {
        this.listaDePacientes = listaDePacientes;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return listaDePacientes.size();
    }

    @Override
    public Object getItem(int position) {
        return listaDePacientes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Paciente paciente = listaDePacientes.get(position);
        View itemLista = LayoutInflater.from(ctx).inflate(R.layout.item_list_pacientes, null);
        TextView txtNomeDoPaciente = (TextView) itemLista.findViewById(R.id.txtNomePacienteList);
        txtNomeDoPaciente.setText(paciente.getUsuario().getNome());
        return itemLista;
    }
}
