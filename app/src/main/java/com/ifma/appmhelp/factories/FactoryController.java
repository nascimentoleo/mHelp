package com.ifma.appmhelp.factories;

import android.content.Context;

import com.ifma.appmhelp.daos.IDao;
import com.ifma.appmhelp.daos.MedicosDao;
import com.ifma.appmhelp.daos.PacientesDao;
import com.ifma.appmhelp.daos.UsuariosDao;
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
            return new UsuariosDao(ctx);
        else if (modelo.getClass() == Medico.class)
            return new MedicosDao(ctx);
        else if (modelo.getClass() == Paciente.class)
            return new PacientesDao(ctx);

        return null;
    }
}
