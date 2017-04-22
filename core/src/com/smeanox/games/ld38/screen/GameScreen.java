package com.smeanox.games.ld38.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.io.IOAnimation;
import com.smeanox.games.ld38.world.SpaceStation;
import com.smeanox.games.ld38.world.modules.Module;

public class GameScreen implements Screen {

	private SpriteBatch batch;
	private Camera camera;
	private float time, scale;
	private int wWidth, wHeight;

	public GameScreen() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
	}

	@Override
	public void show() {

	}

	private void update(float delta){
	}

	private void updateInput(float delta){
		if(Gdx.input.isKeyPressed(Input.Keys.W)){
			camera.translate(0, Consts.CAMERA_SPEED * delta, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)){
			camera.translate(0, -Consts.CAMERA_SPEED * delta, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)){
			camera.translate(Consts.CAMERA_SPEED * delta, 0, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			camera.translate(-Consts.CAMERA_SPEED * delta, 0, 0);
		}
	}

	private void draw(float delta){
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (Module module : SpaceStation.get().getModules()) {
			module.draw(batch, time);
		}
		batch.draw(IOAnimation.Dude1.keyFrame(time), 0, 0, 1, 1);
		batch.end();
	}

	@Override
	public void render(float delta) {
		time += delta;
		updateInput(delta);
		update(delta);
		draw(delta);
	}

	@Override
	public void resize(int width, int height) {
		scale = height / (float) Consts.DESIGN_HEIGHT;
		if(scale >= 0.5f){
			scale = Math.round(scale);
		}
		wWidth = width;
		wHeight = height;
		camera.viewportWidth = width / scale / Consts.SPRITE_SIZE;
		camera.viewportHeight = height / scale / Consts.SPRITE_SIZE;
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
