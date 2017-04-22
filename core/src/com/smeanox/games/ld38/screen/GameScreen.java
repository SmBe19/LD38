package com.smeanox.games.ld38.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.io.IOAnimation;
import com.smeanox.games.ld38.io.IOFont;
import com.smeanox.games.ld38.world.SpaceStation;
import com.smeanox.games.ld38.world.modules.MainModule;
import com.smeanox.games.ld38.world.modules.Module;
import com.smeanox.games.ld38.world.modules.SolarModule;

public class GameScreen implements Screen {

	private SpriteBatch batch;
	private Camera camera;
	private float time, scale;
	private int wWidth, wHeight;

	private Class<? extends Module> buildClazz;
	private Module buildModule, buildNeighbor;
	private int buildX, buildY, buildDirection, buildRotation;
	private boolean[] wasDown = new boolean[2];

	public GameScreen() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();

		initBuild();
	}

	private void initBuild(){
		buildClazz = null;
		buildModule = null;
		buildNeighbor = null;
		buildX = Integer.MAX_VALUE;
		buildY = Integer.MAX_VALUE;
		buildRotation = 0;
	}

	@Override
	public void show() {

	}

	private void update(float delta){
	}

	private void findBuildParams(float mousex, float mousey){
		buildModule = null;
		buildNeighbor = null;
		buildDirection = 0;
		float minVal = Float.MAX_VALUE;
		for(int x = buildX - Consts.BUILD_SEARCH_DIST; x <= buildX + Consts.BUILD_SEARCH_DIST; x++){
			for(int y = buildY - Consts.BUILD_SEARCH_DIST; y <= buildY + Consts.BUILD_SEARCH_DIST; y++){
				Module neighbor = SpaceStation.get().getModule(x, y);
				if (neighbor != null) {
					for(int i = 0; i < 4; i++){
						Module module;
						if (buildClazz == SolarModule.class) {
							module = neighbor.tryAddSolar(i, buildX, buildY);
						} else {
							module = neighbor.tryAddNeighbor(i, buildRotation, buildClazz);
						}
						if (module != null) {
							float xdist = (module.getModuleLocation().getRotX() + module.getModuleLocation().getRotWidth() / 2.f - mousex);
							float ydist = (module.getModuleLocation().getRotY() + module.getModuleLocation().getRotHeight() / 2.f - mousey);
							float val = xdist * xdist + ydist * ydist;
							if(val < minVal){
								buildModule = module;
								buildNeighbor = neighbor;
								buildDirection = i;
								minVal = val;
							}
						}
					}
				}
			}
		}
	}

	private void updateInput(float delta){
		// build
		boolean buildChanged = false;
		if(buildClazz != null) {
			int x, y;
			Vector3 mouse = unprojectMouse();
			x = (int) Math.floor(mouse.x);
			y = (int) Math.floor(mouse.y);
			if (x != buildX || y != buildY) {
				buildX = x;
				buildY = y;
				buildChanged = true;
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
				buildRotation = (buildRotation + 1) % 4;
				buildChanged = true;
			}
			if (buildChanged) {
				findBuildParams(mouse.x, mouse.y);
			}
			if (buildModule != null && wasDown[0] && !Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
				if (buildClazz == SolarModule.class) {
					buildNeighbor.addSolar(buildDirection, buildX, buildY);
				} else {
					buildNeighbor.addNeighbor(buildDirection, buildRotation, buildClazz);
				}
				initBuild();
			}
			if (wasDown[1] && !Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
				initBuild();
			}
		}
		// view
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

		wasDown[0] = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
		wasDown[1] = Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
	}

	private Vector3 unprojectMouse() {
		return camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
	}

	private void draw(float delta){
		Gdx.gl.glClearColor(0, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (Module module : SpaceStation.get().getModules()) {
			module.drawBackground(batch, time);
		}
		batch.draw(IOAnimation.Dude1.keyFrame(time), 0, 0, 1, 1);
		for (Module module : SpaceStation.get().getModules()) {
			module.drawForeground(batch, time);
		}
		if (buildModule != null) {
			batch.setColor(1, 1, 1, 0.5f);
			buildModule.drawBackground(batch, time);
			buildModule.drawForeground(batch, time);
			batch.setColor(1, 1, 1, 1);
		}
		IOFont.grusigPunktBdf.draw(batch, 0, 0, "Hallo Welt");
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
