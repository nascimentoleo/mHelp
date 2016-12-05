package com.ifma.appmhelp.factories;

import android.support.v4.app.Fragment;

import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.Medico;
import com.ifma.appmhelp.views.AlteraMedicoFragment;
import com.ifma.appmhelp.views.AlteraPacienteFragment;

/**
 * Created by leo on 12/5/16.
 */
public class FactoryAlteraDadosActivity {

    public static Fragment getFragment(IModel usuarioLogado){
        if(usuarioLogado.getClass() == Medico.class)
            return new AlteraMedicoFragment();
        return new AlteraPacienteFragment();
    }
}
