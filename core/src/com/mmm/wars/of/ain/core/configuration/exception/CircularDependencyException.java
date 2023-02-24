package com.mmm.wars.of.ain.core.configuration.exception;

public class CircularDependencyException extends ComponentInitializationException {

    public CircularDependencyException(String message) {
        super(message);
    }

    public CircularDependencyException(String message, Throwable cause) {
        super(message, cause);
    }
}
