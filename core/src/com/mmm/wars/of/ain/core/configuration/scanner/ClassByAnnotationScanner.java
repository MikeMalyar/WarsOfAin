package com.mmm.wars.of.ain.core.configuration.scanner;

import com.mmm.wars.of.ain.core.util.reflection.ReflectionAPIUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassByAnnotationScanner {

    private static final String DEFAULT_PACKAGE_TO_SCAN = "com.mmm.wars.of.ain.core";

    public ClassByAnnotationScanner() {
    }

    public List<Class<?>> scan(Class<? extends java.lang.annotation.Annotation> annotationClass,
            String... packagesToScan) {
        List<String> allPackages = new ArrayList<>();
        allPackages.add(DEFAULT_PACKAGE_TO_SCAN);
        allPackages.addAll(Arrays.asList(packagesToScan));

        List<Class<?>> classes = new ArrayList<>();
        for (String rootPackageName : allPackages) {
            classes.addAll(
                    ReflectionAPIUtils.fetchClassesFromPackageMarkedWithAnnotation(rootPackageName, annotationClass));
        }
        return classes;
    }
}
