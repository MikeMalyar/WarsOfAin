package com.mmm.wars.of.ain.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mmm.wars.of.ain.core.configuration.AppContext;

public class WarsOfAinApplicationAdapter extends ApplicationAdapter {

	private AppContext appContext;

	private boolean loading;
	
	@Override
	public void create () {
		appContext = new AppContext();
		appContext.init();

		loading = true;
	}

	@Override
	public void render () {
		if (loading && appContext.getAssetManager().update()) {
			loading = false;
			appContext.getModelFactory().instantiateModels();
		}
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	@Override
	public void dispose () {

	}
}
