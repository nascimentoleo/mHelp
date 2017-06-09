package com.ifma.appmhelp.factories;

import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.Medico;
import com.ifma.appmhelp.views.MedicoActivity;
import com.ifma.appmhelp.views.PacienteActivity;

/**
 * Created by leo on 12/5/16.
 */
public class FactoryLogadoActivity {

    public static Class getActivityClass(IModel usuarioLogado){
        if(usuarioLogado.getClass() == Medico.class)
            return MedicoActivity.class;
        return PacienteActivity.class;
    }
}
