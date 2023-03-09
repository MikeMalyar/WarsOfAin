package com.mmm.wars.of.ain.core.configuration.factory.model;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.mmm.wars.of.ain.core.configuration.annotation.model.ModelObject;
import com.mmm.wars.of.ain.core.configuration.annotation.model.ModelObjectConstructor;
import com.mmm.wars.of.ain.core.configuration.factory.model.injection.provider.ParameterInjectionStrategyProvider;
import com.mmm.wars.of.ain.core.configuration.scanner.ClassByAnnotationScanner;
import com.mmm.wars.of.ain.core.domain.model.type.ModelType;
import com.mmm.wars.of.ain.core.object.AbstractGameObject;
import com.mmm.wars.of.ain.core.util.reflection.ReflectionAPIUtils;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModelFactory {

    private ClassByAnnotationScanner scanner;
    private ParameterInjectionStrategyProvider parameterInjectionStrategyProvider;
    private AssetManager assetManager;

    private List<Class<?>> modelClasses;
    private Map<ModelType, AbstractGameObject> models;

    public ModelFactory(ParameterInjectionStrategyProvider parameterInjectionStrategyProvider,
            AssetManager assetManager) {
        scanner = new ClassByAnnotationScanner();

        this.parameterInjectionStrategyProvider = parameterInjectionStrategyProvider;
        this.assetManager = assetManager;

        models = new HashMap<>();
    }

    public void init() {
        modelClasses = scanner.scan(ModelObject.class);

        for (Class<?> modelClass : modelClasses) {
            String modelPath = ReflectionAPIUtils.findAnnotation(modelClass, ModelObject.class).modelPath();

            assetManager.load(modelPath, Model.class);
        }
    }

    public void instantiateModels() {
        for (Class<?> modelClass : modelClasses) {
            List<Constructor> constructors = ReflectionAPIUtils
                    .getConstructorsMarkedWithAnnotation(modelClass, ModelObjectConstructor.class);
            if (constructors.size() != 1) {
                throw new IllegalStateException(String
                        .format("Model class %s has no or more than one constructor for Model injection",
                                modelClass.getName()));
            }

            ModelType modelType = ReflectionAPIUtils.findAnnotation(modelClass, ModelObject.class).modelType();

            Constructor constructor = constructors.get(0);
            List<Object> parameterValues = Arrays.stream(constructor.getParameters())
                    .map(parameter -> parameterInjectionStrategyProvider.getStrategyByParameter(parameter)
                            .doInjection(modelClass, parameter))
                    .collect(Collectors.toList());

            models.put(modelType, ReflectionAPIUtils.initializeObjectByConstructor(constructor, parameterValues));
        }
    }
}
