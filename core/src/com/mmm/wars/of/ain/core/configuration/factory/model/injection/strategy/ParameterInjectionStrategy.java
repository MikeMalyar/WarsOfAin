package com.mmm.wars.of.ain.core.configuration.factory.model.injection.strategy;

import java.lang.reflect.Parameter;

public interface ParameterInjectionStrategy {

    Object doInjection(Class<?> targetClass, Parameter parameter);
}
