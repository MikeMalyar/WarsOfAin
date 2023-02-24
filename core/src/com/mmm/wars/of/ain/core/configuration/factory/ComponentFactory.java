package com.mmm.wars.of.ain.core.configuration.factory;

import com.mmm.wars.of.ain.core.configuration.annotation.Component;
import com.mmm.wars.of.ain.core.configuration.annotation.Injected;
import com.mmm.wars.of.ain.core.configuration.component.ComponentDefinition;
import com.mmm.wars.of.ain.core.configuration.exception.CircularDependencyException;
import com.mmm.wars.of.ain.core.configuration.exception.ComponentInitializationException;
import com.mmm.wars.of.ain.core.configuration.scanner.ComponentScanner;
import com.mmm.wars.of.ain.core.util.reflection.ReflectionAPIUtils;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ComponentFactory {

    private ComponentScanner componentScanner;

    private Map<Class<?>, ComponentDefinition> componentDefinitions;

    public ComponentFactory() {
        componentScanner = new ComponentScanner();

        componentDefinitions = new HashMap<>();
    }

    public void init(String ...rootPackageNames) {
        initializeAllComponents(rootPackageNames);
    }

    private void initializeAllComponents(String ...rootPackageNames) {
        try {
            Map<Integer, List<Class<?>>> dependencyTree = buildDependencyTree(componentScanner.scan(rootPackageNames));

            for (int i = 0; i < dependencyTree.size(); ++i) {

                for (Class<?> componentClass : dependencyTree.get(i)) {
                    String componentId = UUID.randomUUID().toString();
                    if (componentClass.getAnnotation(Component.class) != null) {
                        componentId = componentClass.getAnnotation(Component.class).id();
                    }
                    ComponentDefinition componentDefinition = new ComponentDefinition()
                            .setComponentClass(componentClass)
                            .setComponentId(componentId);

                    List<Constructor> constructors = ReflectionAPIUtils
                            .getConstructorsMarkedWithAnnotation(componentClass, Injected.class);
                    if (constructors.isEmpty()) {
                        Object instance = ReflectionAPIUtils.initializeObjectByClass(componentClass);
                        if (instance != null) {
                            componentDefinition.setInstance(instance);
                            try {
                                componentDefinition.setConstructorToInitialize(componentClass.getDeclaredConstructor());
                            } catch (NoSuchMethodException e) {
                                throw new ComponentInitializationException(String
                                        .format("Component %s has no default constructor so cannot be instantiated",
                                                componentClass.getName()));
                            }
                        } else {
                            throw new ComponentInitializationException(
                                    String.format("Component %s has no default constructor so cannot be instantiated",
                                            componentClass.getName()));
                        }
                    } else {
                        List<Class<?>> parameters = Arrays.asList(constructors.get(0).getParameterTypes());
                        List<Object> values = parameters.stream()
                                .map(parameter -> componentDefinitions.get(parameter).getInstance())
                                .collect(Collectors.toList());
                        Object instance = ReflectionAPIUtils.initializeObjectByClass(componentClass, parameters, values);
                        if (instance != null) {
                            componentDefinition.setInstance(instance);
                            try {
                                componentDefinition.setConstructorToInitialize(
                                        componentClass.getConstructor(parameters.toArray(new Class<?>[0])));
                                componentDefinition.setConstructorValues(values.toArray(new Object[0]));
                            } catch (NoSuchMethodException e) {
                                throw new ComponentInitializationException(String
                                        .format("Component %s cannot be instantiated with provided constructor",
                                                componentClass.getName()));
                            }
                        } else {
                            throw new ComponentInitializationException(
                                    String.format("Component %s cannot be instantiated with provided constructor",
                                            componentClass.getName()));
                        }
                    }
                    componentDefinitions.put(componentClass, componentDefinition);
                }
            }
        } catch (ComponentInitializationException e) {
            throw new IllegalStateException(e);  // TODO handle this
        }
    }

    private Map<Integer, List<Class<?>>> buildDependencyTree(List<Class<?>> inputClasses)
            throws ComponentInitializationException {
        Map<Integer, List<Class<?>>> dependencyTree = new HashMap<>();
        List<Class<?>> classes = new ArrayList<>(inputClasses);
        List<Class<?>> classesToRemove = new ArrayList<>();

        for (Class<?> clazz : classes) {
            List<Constructor> constructors = ReflectionAPIUtils
                    .getConstructorsMarkedWithAnnotation(clazz, Injected.class);
            if (constructors.size() > 1) {
                throw new ComponentInitializationException(
                        String.format("Component %s has more than one injectable constructor", clazz.getName()));
            } else if (constructors.size() == 0) {
                classesToRemove.add(clazz);
            } else {
                List<Class<?>> parameters = Arrays.asList(constructors.get(0).getParameterTypes());
                if (!classes.containsAll(parameters)) {
                    throw new ComponentInitializationException(
                            String.format("Component %s constructor contains parameters which are not components",
                                    clazz.getName()));
                }
            }
        }
        dependencyTree.computeIfAbsent(0, k -> new ArrayList<>());
        dependencyTree.get(0).addAll(classesToRemove);
        classes.removeAll(classesToRemove);

        int level = 1;
        while (!classes.isEmpty()) {
            classesToRemove.clear();
            int finalLevel = level;
            for (Class<?> clazz : classes) {
                List<Constructor> constructors = ReflectionAPIUtils
                        .getConstructorsMarkedWithAnnotation(clazz, Injected.class);
                List<Class<?>> parameters = Arrays.asList(constructors.get(0).getParameterTypes());
                if (dependencyTree.entrySet()
                        .stream()
                        .filter(dependency -> dependency.getKey() < finalLevel)
                        .map(Map.Entry::getValue)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList())
                        .containsAll(parameters)) {
                    classesToRemove.add(clazz);
                }
            }
            if (!classesToRemove.isEmpty()) {
                dependencyTree.computeIfAbsent(level, k -> new ArrayList<>());
                dependencyTree.get(level).addAll(classesToRemove);
                classes.removeAll(classesToRemove);
            } else {
                throw new CircularDependencyException(String
                        .format("Cannot initialize components due to circular reference in one of the classes: %s",
                                classes));
            }
            ++level;
        }

        return dependencyTree;
    }
}
