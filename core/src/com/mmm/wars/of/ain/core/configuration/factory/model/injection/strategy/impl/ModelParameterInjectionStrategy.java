package com.mmm.wars.of.ain.core.configuration.factory.model.injection.strategy.impl;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.mmm.wars.of.ain.core.configuration.annotation.model.ModelObject;
import com.mmm.wars.of.ain.core.configuration.factory.model.injection.strategy.ParameterInjectionStrategy;
import com.mmm.wars.of.ain.core.util.reflection.ReflectionAPIUtils;
import java.lang.reflect.Parameter;

public class ModelParameterInjectionStrategy implements ParameterInjectionStrategy {

    private AssetManager assetManager;

    public ModelParameterInjectionStrategy(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public Object doInjection(Class<?> targetClass, Parameter parameter) {
        ModelObject annotation = ReflectionAPIUtils.findAnnotation(targetClass, ModelObject.class);
        if (annotation != null) {
            String modelPath = annotation.modelPath();
            return assetManager.get(modelPath, Model.class);
        }
        return null;
    }
}
