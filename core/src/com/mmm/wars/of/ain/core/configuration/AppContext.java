package com.mmm.wars.of.ain.core.configuration;

import com.mmm.wars.of.ain.core.configuration.factory.ComponentFactory;

public class AppContext {

    private ComponentFactory componentFactory;

    public AppContext() {
        componentFactory = new ComponentFactory();
    }

    public void init() {
        componentFactory.init();
    }
}
