package com.ifma.appmhelp.factories;

import android.content.Context;

import com.ifma.appmhelp.controls.IController;
import com.ifma.appmhelp.controls.MedicosController;
import com.ifma.appmhelp.controls.PacientesController;
import com.ifma.appmhelp.controls.UsuariosController;
import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.Medico;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.Usuario;

/**
 * Created by leo on 12/5/16.
 */
public class FactoryController {

    public static IController getController(Context ctx, IModel modelo){
        if (modelo.getClass() == Usuario.class)
            return new UsuariosController(ctx);
        else if (modelo.getClass() == Medico.class)
            return new MedicosController(ctx);
        else if (modelo.getClass() == Paciente.class)
            return new PacientesController(ctx);

        return null;
    }
}
