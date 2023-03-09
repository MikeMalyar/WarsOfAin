package com.mmm.wars.of.ain.core.configuration.factory.model.injection.strategy.impl;

import com.mmm.wars.of.ain.core.configuration.annotation.model.NodeId;
import com.mmm.wars.of.ain.core.configuration.factory.model.injection.strategy.ParameterInjectionStrategy;
import java.lang.reflect.Parameter;

public class NodeIdParameterInjectionStrategy implements ParameterInjectionStrategy {

    @Override
    public Object doInjection(Class<?> targetClass, Parameter parameter) {
        return parameter.getAnnotation(NodeId.class).value();
    }
}
