package com.ifma.appmhelp.factories;

import android.content.Context;

import com.ifma.appmhelp.daos.IDao;
import com.ifma.appmhelp.daos.MedicoDao;
import com.ifma.appmhelp.daos.PacienteDao;
import com.ifma.appmhelp.daos.UsuarioDao;
import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.Medico;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.Usuario;

/**
 * Created by leo on 12/5/16.
 */
public class FactoryController {

    public static IDao getController(Context ctx, IModel modelo){
        if (modelo.getClass() == Usuario.class)
            return new UsuarioDao(ctx);
        else if (modelo.getClass() == Medico.class)
            return new MedicoDao(ctx);
        else if (modelo.getClass() == Paciente.class)
            return new PacienteDao(ctx);

        return null;
    }
}
