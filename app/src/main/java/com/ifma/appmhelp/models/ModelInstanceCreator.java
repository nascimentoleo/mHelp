package com.ifma.appmhelp.models;

import com.google.gson.InstanceCreator;

import java.lang.reflect.Type;

/**
 * Created by leo on 12/21/16.
 */

public class ModelInstanceCreator implements InstanceCreator <IModel>{

    @Override
    public IModel createInstance(Type type) {
      return null;
    }
}
