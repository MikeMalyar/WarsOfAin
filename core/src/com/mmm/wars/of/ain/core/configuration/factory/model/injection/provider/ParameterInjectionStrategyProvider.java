package com.mmm.wars.of.ain.core.configuration.factory.model.injection.provider;

import com.mmm.wars.of.ain.core.configuration.factory.model.injection.strategy.ParameterInjectionStrategy;
import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class ParameterInjectionStrategyProvider {

    private Map<Class<?>, ParameterInjectionStrategy> strategiesByParameterClass;
    private Map<Class<? extends java.lang.annotation.Annotation>, ParameterInjectionStrategy>
            strategiesByParameterAnnotation;

    public ParameterInjectionStrategyProvider() {
        strategiesByParameterClass = new HashMap<>();
        strategiesByParameterAnnotation = new HashMap<>();
    }

    public void registerStrategyByParameterClass(Class<?> parameterClass, ParameterInjectionStrategy strategy) {
        strategiesByParameterClass.put(parameterClass, strategy);
    }

    public void registerStrategyByParameterAnnotation(Class<? extends java.lang.annotation.Annotation> annotationClass,
            ParameterInjectionStrategy strategy) {
        strategiesByParameterAnnotation.put(annotationClass, strategy);
    }

    public ParameterInjectionStrategy getStrategyByParameter(Parameter parameter) {
        for (Annotation annotation : parameter.getAnnotations()) {
            if (strategiesByParameterAnnotation.containsKey(annotation.annotationType())) {
                return strategiesByParameterAnnotation.get(annotation.annotationType());
            }
        }
        if (strategiesByParameterClass.containsKey(parameter.getType())) {
            return strategiesByParameterClass.get(parameter.getType());
        }
        throw new IllegalStateException(String.format("Parameter class %s or annotations are not registered",
                parameter.getType().getName()));
    }
}
