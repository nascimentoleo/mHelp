package com.ifma.appmhelp.lib;

import com.ifma.appmhelp.models.IModel;

import java.util.Comparator;

/**
 * Created by leo on 1/6/17.
 */

public class ModelComparator implements Comparator<IModel> {

    @Override
    public int compare(IModel objeto1, IModel objeto2) {
        //return objeto1.getId() < objeto2.getId() ? -1 : 0;
         return objeto1.getId() > objeto2.getId() ? 1
                : objeto1.getId() < objeto2.getId() ? -1
                : 0;
    }

}
