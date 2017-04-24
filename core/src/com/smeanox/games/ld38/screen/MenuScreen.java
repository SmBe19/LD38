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
import com.smeanox.games.ld38.io.IOFont;
import com.smeanox.games.ld38.io.IOTexture;
import com.smeanox.games.ld38.world.SpaceStation;

public class MenuScreen implements Screen {

	private Camera camera;
	private SpriteBatch batch;
	private float wWidth, wHeight;
	private boolean wasDown;
	private String[] creditsSplit;

	public MenuScreen() {
		camera = new OrthographicCamera();
		batch = new SpriteBatch();
		creditsSplit = Consts.GAME_CREDITS.split("\n");
	}

	@Override
	public void show() {
		wasDown = false;
	}

	protected void drawCentered(SpriteBatch batch, String text, int y, int scale) {
		float width = IOFont.grusigPunktBdf.width(scale * Consts.SPRITE_SIZE, text);
		IOFont.grusigPunktBdf.draw(batch, (int) (-width / 2), y, scale * Consts.SPRITE_SIZE, text, -1);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.setColor(1, 1, 1, 1);
		for(int ax = 0; ax < wWidth; ax += Consts.DESIGN_WIDTH){
			for(int ay = 0; ay < wHeight; ay += Consts.DESIGN_HEIGHT){
				batch.draw(IOTexture.bg.texture, ax - wWidth / 2, ay - wHeight / 2, Consts.DESIGN_WIDTH, Consts.DESIGN_HEIGHT);
			}
		}
		batch.draw(IOAnimation.Rocket.texture(), -Consts.SPRITE_SIZE, -Consts.SPRITE_SIZE * 5f, 1 * Consts.SPRITE_SIZE, 2.5f * Consts.SPRITE_SIZE, 2 * Consts.SPRITE_SIZE, 5 * Consts.SPRITE_SIZE, 1, 1, 90);
		batch.setColor(1, 1, 0, 1);
		drawCentered(batch, Consts.GAME_NAME, -Consts.SPRITE_SIZE * 5, 3);
		int ay = 24;
		for (String line : creditsSplit) {
			drawCentered(batch, line, ay, 2);
			ay += 36;
		}
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
		camera.viewportWidth = width / scale;
		camera.viewportHeight = height / scale;
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
