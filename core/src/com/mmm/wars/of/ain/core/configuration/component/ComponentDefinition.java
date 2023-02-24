package com.mmm.wars.of.ain.core.configuration.component;

import com.mmm.wars.of.ain.core.configuration.annotation.Component;
import com.mmm.wars.of.ain.core.util.reflection.ReflectionAPIUtils;
import java.lang.reflect.Constructor;
import java.util.Arrays;

public class ComponentDefinition {

    private Class<?> componentClass;
    private String componentId;
    private Constructor constructorToInitialize;
    private Object[] constructorValues;

    private Object instance;

    public Class<?> getComponentClass() {
        return componentClass;
    }

    public ComponentDefinition setComponentClass(Class<?> componentClass) {
        this.componentClass = componentClass;
        return this;
    }

    public String getComponentId() {
        return componentId;
    }

    public ComponentDefinition setComponentId(String componentId) {
        this.componentId = componentId;
        return this;
    }

    public Constructor getConstructorToInitialize() {
        return constructorToInitialize;
    }

    public ComponentDefinition setConstructorToInitialize(Constructor constructorToInitialize) {
        this.constructorToInitialize = constructorToInitialize;
        return this;
    }

    public Object[] getConstructorValues() {
        return constructorValues;
    }

    public ComponentDefinition setConstructorValues(Object[] constructorValues) {
        this.constructorValues = constructorValues;
        return this;
    }

    public Object getInstance() {
        if (ComponentStrategy.FACTORY.equals(componentClass.getAnnotation(Component.class).strategy())) {
            return ReflectionAPIUtils.initializeObjectByClass(componentClass,
                    Arrays.asList(constructorToInitialize.getParameterTypes()),
                    Arrays.asList(constructorValues));
        }
        return instance;
    }

    public ComponentDefinition setInstance(Object instance) {
        this.instance = instance;
        return this;
    }
}
