package com.mmm.wars.of.ain.core.util.reflection;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ReflectionAPIUtils {

    private ReflectionAPIUtils() {}

    public static List<Class<?>> fetchClassesFromPackage(String packageName) {

        InputStream inputStream = ClassLoader.
                getSystemResourceAsStream(packageName.replace(".", "/"));

        if (inputStream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            return reader.lines()
                    .map(line -> {
                        if (line.endsWith(".class")) {
                            Class<?> clazz = getClassByName(line, packageName);
                            return clazz == null ? List.<Class<?>>of() : List.of(clazz);
                        } else {
                            return fetchClassesFromPackage(packageName + "." + line);
                        }
                    })
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public static List<Class<?>> fetchClassesFromPackageMarkedWithAnnotation(String packageName,
            Class<? extends java.lang.annotation.Annotation> annotationClass) {
        List<Class<?>> allClasses = fetchClassesFromPackage(packageName);
        return allClasses.stream()
                .filter(clazz -> !clazz.isAnnotation())
                .filter(clazz -> hasAnnotation(clazz, annotationClass))
                .collect(Collectors.toList());
    }

    public static List<Class<?>> fetchClassesFromPackageHasAnnotatedFields(String packageName,
            Class<? extends java.lang.annotation.Annotation> annotationClass) {
        List<Class<?>> allClasses = fetchClassesFromPackage(packageName);
        return allClasses.stream()
                .filter(clazz -> Arrays.stream(clazz.getDeclaredFields())
                        .anyMatch(field -> field.isAnnotationPresent(annotationClass)))
                .collect(Collectors.toList());
    }

    public static <T> T initializeObjectByClass(Class<T> clazz) {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            constructor.setAccessible(true);
            return (T) constructor.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException
                | IllegalAccessException e) {
            return null;
        }
    }

    public static <T> T initializeObjectByClass(Class<T> clazz, List<Class<?>> parameterTypes,
            List<Object> parameterValues) {
        try {
            Constructor<?> constructor = clazz.getConstructor(parameterTypes.toArray(new Class<?>[0]));
            constructor.setAccessible(true);
            return (T) constructor.newInstance(parameterValues.toArray());
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException
                | IllegalAccessException e) {
            return null;
        }
    }

    public static <T> T initializeObjectByConstructor(Constructor constructor, List<Object> parameterValues) {
        try {
            constructor.setAccessible(true);
            return (T) constructor.newInstance(parameterValues.toArray());
        } catch (InvocationTargetException | InstantiationException
                | IllegalAccessException e) {
            return null;
        }
    }

    public static <T> boolean updateObjectFieldsMarkedWithAnnotation(Object object,
            Class<? extends java.lang.annotation.Annotation> annotationClass, Function<Field, T> valueFunction)
            throws IllegalAccessException {
        return updateObjectFieldsMarkedWithAnnotation(object, object.getClass(), annotationClass, valueFunction);
    }

    public static <T> boolean updateObjectFieldsMarkedWithAnnotation(Object object, Class<?> objectClass,
            Class<? extends java.lang.annotation.Annotation> annotationClass, Function<Field, T> valueFunction)
            throws IllegalAccessException {
        boolean hasUpdated = false;
        do {
            for (Field field : objectClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(annotationClass)) {
                    field.setAccessible(true);
                    field.set(object, valueFunction.apply(field));
                    hasUpdated = true;
                }
            }
        } while ((objectClass = objectClass.getSuperclass()) != Object.class);
        return hasUpdated;
    }

    public static boolean hasAnnotation(Class<?> clazz,
            Class<? extends java.lang.annotation.Annotation> annotationClass) {
        return findAnnotation(clazz, annotationClass) != null;
    }

    public static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType) {
        return findAnnotation(clazz, annotationType, new HashSet<Annotation>());
    }

    @SuppressWarnings("unchecked")
    private static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType,
            Set<Annotation> visited) {
        try {
            Annotation[] declaredAnnotations = clazz.getDeclaredAnnotations();
            for (Annotation annotation : declaredAnnotations) {
                if (annotation.annotationType() == annotationType) {
                    return (A) annotation;
                }
            }
            for (Annotation annotation : declaredAnnotations) {
                if (visited.add(annotation)) {
                    A foundAnnotation = findAnnotation(annotation.annotationType(), annotationType, visited);
                    if (foundAnnotation != null) {
                        return foundAnnotation;
                    }
                }
            }
        }
        catch (Exception ex) {
            return null;
        }

        for (Class<?> classInterface : clazz.getInterfaces()) {
            A annotation = findAnnotation(classInterface, annotationType, visited);
            if (annotation != null) {
                return annotation;
            }
        }

        Class<?> superclass = clazz.getSuperclass();
        if (superclass == null || Object.class == superclass) {
            return null;
        }
        return findAnnotation(superclass, annotationType, visited);
    }

    public static List<Constructor> getConstructorsMarkedWithAnnotation(Class<?> clazz,
            Class<? extends java.lang.annotation.Annotation> annotationClass) {
        return Arrays.stream(clazz.getConstructors())
                .filter(constructor -> constructor.isAnnotationPresent(annotationClass))
                .collect(Collectors.toList());
    }

    public static Class<?> getClassByName(String classFileName, String packageName) {
        try {
            return Class.forName(packageName + "." + classFileName.substring(0, classFileName.lastIndexOf(".")));
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
