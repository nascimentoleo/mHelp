package com.ifma.appmhelp.db;

import com.ifma.appmhelp.models.Medico;
import com.ifma.appmhelp.models.MedicoPaciente;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.Usuario;

/**
 * Created by leo on 11/29/16.
 */
public class DbClass {

    private static final Class<?>[] classes = new Class[] {
            Usuario.class,
            Medico.class,
            Paciente.class,
            MedicoPaciente.class};


    public static Class<?>[] getClasses() {
        return classes;
    }
}
