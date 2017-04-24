package com.smeanox.games.ld38.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.LD38;
import com.smeanox.games.ld38.io.IOAnimation;
import com.smeanox.games.ld38.io.IOTexture;
import com.smeanox.games.ld38.world.SpaceStation;

public class MenuScreen implements Screen {

	private Camera camera;
	private SpriteBatch batch;
	private float wWidth, wHeight;
	private boolean wasDown;

	public MenuScreen() {
		camera = new OrthographicCamera();
		batch = new SpriteBatch();
	}

	@Override
	public void show() {
		wasDown = false;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for(int ax = 0; ax < wWidth; ax += Consts.DESIGN_WIDTH / Consts.SPRITE_SIZE){
			for(int ay = 0; ay < wHeight; ay += Consts.DESIGN_HEIGHT / Consts.SPRITE_SIZE){
				batch.draw(IOTexture.bg.texture, ax - wWidth / 2 / Consts.SPRITE_SIZE, ay - wHeight / 2 / Consts.SPRITE_SIZE, Consts.DESIGN_WIDTH / Consts.SPRITE_SIZE, Consts.DESIGN_HEIGHT / Consts.SPRITE_SIZE);
			}
		}
		batch.draw(IOAnimation.Rocket.texture(), -1, -2.5f, 1, 2.5f, 2, 5, 1, 1, 90);
		batch.end();

		if (wasDown && !Gdx.input.isTouched()) {
			SpaceStation.get().init();
			LD38.me.showGameScreen();
		}

		wasDown = Gdx.input.isTouched();
	}

	@Override
	public void resize(int width, int height) {
		float scale = height / (float) Consts.DESIGN_HEIGHT;
		if(scale >= 0.5f){
			scale = Math.round(scale);
		}
		wWidth = width / scale;
		wHeight = height / scale;
		camera.viewportWidth = width / scale / Consts.SPRITE_SIZE;
		camera.viewportHeight = height / scale / Consts.SPRITE_SIZE;
		camera.update();
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

	}
}
