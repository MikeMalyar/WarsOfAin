package com.mmm.wars.of.ain.core.configuration;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.mmm.wars.of.ain.core.configuration.annotation.model.NodeId;
import com.mmm.wars.of.ain.core.configuration.factory.model.ModelFactory;
import com.mmm.wars.of.ain.core.configuration.factory.model.injection.provider.ParameterInjectionStrategyProvider;
import com.mmm.wars.of.ain.core.configuration.factory.model.injection.strategy.impl.ModelParameterInjectionStrategy;
import com.mmm.wars.of.ain.core.configuration.factory.model.injection.strategy.impl.NodeIdParameterInjectionStrategy;

public class AppContext {

    private ModelFactory modelFactory;

    private ParameterInjectionStrategyProvider parameterInjectionStrategyProvider;

    private AssetManager assetManager;

    public AppContext() {

    }

    public void init() {
        ModelFactory modelFactory = getModelFactory();
        modelFactory.init();
    }

    public ModelFactory getModelFactory() {
        if (modelFactory == null) {
            modelFactory = createModelFactory();
        }
        return modelFactory;
    }

    public ModelFactory createModelFactory() {
        return new ModelFactory(getParameterInjectionStrategyProvider(), getAssetManager());
    }

    public ParameterInjectionStrategyProvider getParameterInjectionStrategyProvider() {
        if (parameterInjectionStrategyProvider == null) {
            parameterInjectionStrategyProvider = createParameterInjectionStrategyProvider();
        }
        return parameterInjectionStrategyProvider;
    }

    public ParameterInjectionStrategyProvider createParameterInjectionStrategyProvider() {
        ParameterInjectionStrategyProvider provider = new ParameterInjectionStrategyProvider();

        provider.registerStrategyByParameterClass(Model.class, new ModelParameterInjectionStrategy(getAssetManager()));

        provider.registerStrategyByParameterAnnotation(NodeId.class, new NodeIdParameterInjectionStrategy());

        return provider;
    }

    public AssetManager getAssetManager() {
        if (assetManager == null) {
            assetManager = createAssetManager();
        }
        return assetManager;
    }

    public AssetManager createAssetManager() {
        return new AssetManager();
    }
}
