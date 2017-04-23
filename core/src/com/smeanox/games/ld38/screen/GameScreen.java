package com.smeanox.games.ld38.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.io.IOFont;
import com.smeanox.games.ld38.world.Dude;
import com.smeanox.games.ld38.world.Resource;
import com.smeanox.games.ld38.world.SpaceStation;
import com.smeanox.games.ld38.world.module.Module;
import com.smeanox.games.ld38.world.module.ModuleFactory;
import com.smeanox.games.ld38.world.module.SolarModule;

import java.util.HashSet;
import java.util.Map;

public class GameScreen implements Screen {

	private SpriteBatch batch;
	private Camera camera, uiCamera;
	private float time, scale;
	private float wWidth, wHeight;
	private Window buildWindow, buildInfoWindow, recourceInfoWindow, moduleWindow, messageWindow;
	private boolean paused;

	private Class<? extends Module> buildClazz, hoverBuildClazz;
	private Module buildModule, buildNeighbor;
	private int buildX, buildY, buildDirection, buildRotation;
	private boolean[] wasDown = new boolean[2];

	public GameScreen() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		uiCamera = new OrthographicCamera();
		paused = false;

		initBuild();
	}

	private void initBuild() {
		buildClazz = null;
		buildModule = null;
		buildNeighbor = null;
		buildX = Integer.MAX_VALUE;
		buildY = Integer.MAX_VALUE;
		buildRotation = 0;
	}

	private void initWindows2000(){
		if (buildWindow != null) {
			Window.windows95.remove(buildWindow);
		}
		if (buildInfoWindow != null) {
			Window.windows95.remove(buildInfoWindow);
		}
		if (recourceInfoWindow != null) {
			Window.windows95.remove(recourceInfoWindow);
		}
		buildWindow = new BuildWindow();
		Window.windows95.add(buildWindow);
		buildInfoWindow = new BuildInfoWindow();
		Window.windows95.add(buildInfoWindow);
		recourceInfoWindow = new ResourceInfoWindow();
		Window.windows95.add(recourceInfoWindow);
	}

	@Override
	public void show() {

	}

	private void update(float delta){
		if(!paused) {
			SpaceStation.get().update(delta);
			if (SpaceStation.get().peekMessage() != null) {
				paused = true;
				messageWindow = new MessageWindow(SpaceStation.get().popMessage());
				Window.windows95.add(messageWindow);
			}
		}
	}

	private void findBuildParams(float mousex, float mousey, boolean allowRotation){
		buildModule = null;
		buildNeighbor = null;
		buildDirection = 0;
		float minVal = Float.MAX_VALUE;
		for (int x = buildX - Consts.BUILD_SEARCH_DIST; x <= buildX + Consts.BUILD_SEARCH_DIST; x++) {
			for (int y = buildY - Consts.BUILD_SEARCH_DIST; y <= buildY + Consts.BUILD_SEARCH_DIST; y++) {
				Module neighbor = SpaceStation.get().getModule(x, y);
				if (neighbor != null) {
					for (int dir = 0; dir < 4; dir++) {
						for(int rot = 0; rot < 4; rot++) {
							Module module;
							if (buildClazz == SolarModule.class) {
								module = neighbor.tryAddSolar(dir, buildX, buildY);
							} else {
								module = neighbor.tryAddNeighbor(dir, rot, buildClazz);
							}
							if (module != null) {
								float xdist = (module.getModuleLocation().getRotX() + module.getModuleLocation().getRotWidth() / 2.f - mousex);
								float ydist = (module.getModuleLocation().getRotY() + module.getModuleLocation().getRotHeight() / 2.f - mousey);
								float val = xdist * xdist + ydist * ydist;
								if (val < minVal) {
									buildModule = module;
									buildNeighbor = neighbor;
									buildDirection = dir;
									buildRotation = rot;
									minVal = val;
								}
							}
						}
					}
				}
			}
		}
		if (buildModule != null) {
			buildModule.setFinished(true);
		}
	}

	private void updateInput(float delta){
		// build
		boolean buildChanged = false;
		int x, y;
		Vector3 mouse;
		boolean[] isMouseDown = new boolean[2];
		isMouseDown[0] = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
		isMouseDown[1] = Gdx.input.isButtonPressed(Input.Buttons.RIGHT);

		mouse = unprojectMouseUI();
		x = (int) Math.floor(mouse.x);
		y = (int) Math.floor(mouse.y);
		for (Window window : new HashSet<Window>(Window.windows95)) {
			window.update(delta, x, y, isMouseDown);
		}

		if(!paused) {
			mouse = unprojectMouse();
			x = (int) Math.floor(mouse.x);
			y = (int) Math.floor(mouse.y);
			if (buildClazz != null) {
				if (x != buildX || y != buildY) {
					buildX = x;
					buildY = y;
					buildChanged = true;
				}
				if (buildChanged) {
					findBuildParams(mouse.x, mouse.y, true);
				}
				if (buildModule != null && wasDown[0] && !isMouseDown[0]) {
					if (SpaceStation.get().buyModule(buildClazz, false)) {
						if (buildClazz == SolarModule.class) {
							buildNeighbor.addSolar(buildDirection, buildX, buildY);
						} else {
							buildNeighbor.addNeighbor(buildDirection, buildRotation, buildClazz);
						}
						findBuildParams(mouse.x, mouse.y, true);
					}
				}
				if (wasDown[1] && !isMouseDown[1]) {
					initBuild();
				}
			} else {
				if (wasDown[0] && !isMouseDown[0]) {
					if (moduleWindow != null) {
						Window.windows95.remove(moduleWindow);
						moduleWindow = null;
					}
					Module module = SpaceStation.get().getModule(x, y);
					if (module != null && module.isFinished()) {
						Vector3 mouse2 = unprojectMouseUI();
						moduleWindow = module.createWindow(mouse2.x, mouse2.y);
						if (moduleWindow != null) {
							Window.windows95.add(moduleWindow);
						}
					}
				}
				if (wasDown[1] && !isMouseDown[0]) {
					if (moduleWindow != null) {
						Window.windows95.remove(moduleWindow);
						moduleWindow = null;
					}
				}
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

		// cheats
		if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
			for (Resource resource : Resource.values()) {
				SpaceStation.get().getResources().get(resource).value = Float.POSITIVE_INFINITY;
				SpaceStation.get().getResourceMax().get(resource).value = Float.POSITIVE_INFINITY;
			}
		}

		wasDown = isMouseDown;
	}

	private Vector3 unprojectMouse() {
		return camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
	}

	private Vector3 unprojectMouseUI() {
		return uiCamera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
	}

	private void draw(float delta){
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.setColor(1, 1, 1, 1);
		batch.begin();
		for (Module module : SpaceStation.get().getModules()) {
			module.drawBackground(batch, time);
		}
		for (Dude dude : SpaceStation.get().getDudes()) {
			dude.draw(batch, time);
		}
		for (Module module : SpaceStation.get().getModules()) {
			module.drawForeground(batch, time);
		}
		if (buildModule != null) {
			Color oldColor = batch.getColor().cpy();
			batch.setColor(1, 1, 1, Consts.BUILD_PREVIEW_ALPHA);
			buildModule.drawBackground(batch, time);
			buildModule.drawForeground(batch, time);
			batch.setColor(oldColor);
		}
		for (Dude dude : SpaceStation.get().getDudes()) {
			dude.drawUI(batch, time);
		}
		batch.end();

		drawUI(delta);
	}

	private void drawUI(float delta) {
		batch.setProjectionMatrix(uiCamera.combined);
		batch.begin();
		for (Window window : Window.windows95) {
			window.draw(batch, delta);
		}
		IOFont.grusigPunktBdf.draw(batch, 16, 16, Consts.SPRITE_SIZE, Consts.GAME_NAME);
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
		wWidth = width / scale;
		wHeight = height / scale;
		camera.viewportWidth = width / scale / Consts.SPRITE_SIZE;
		camera.viewportHeight = height / scale / Consts.SPRITE_SIZE;
		camera.update();
		uiCamera.viewportWidth = width / scale;
		uiCamera.viewportHeight = height / scale;
		uiCamera.position.set(uiCamera.viewportWidth / 2, uiCamera.viewportHeight / 2, 0);
		uiCamera.update();

		initWindows2000();
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

	private class BuildWindow extends Window {

		public BuildWindow() {
			super(wWidth - 150, 0, 100, 100);
		}

		@Override
		public void init() {
			height = 20 + ModuleFactory.moduleClasses.size() * 20;
			y = wHeight - 100 - height;

			float ay = height - 15;
			for (final Class<? extends Module> module : ModuleFactory.moduleClasses) {
				uiElements.add(new ButtonWithHover(5, ay, 90, 10, ModuleFactory.getModuleName(module), new LabelActionHandler() {
					@Override
					public void actionHappened(Label label, float delta) {
						label.color = SpaceStation.get().buyModule(module, true) ? Color.BLACK : Color.FIREBRICK;
					}
				}, new LabelActionHandler() {
					@Override
					public void actionHappened(Label label, float delta) {
						initBuild();
						buildClazz = module;
						if (moduleWindow != null) {
							Window.windows95.remove(moduleWindow);
							moduleWindow = null;
						}
					}
				}, new LabelActionHandler() {
					@Override
					public void actionHappened(Label label, float delta) {
						hoverBuildClazz = module;
					}
				}));
				ay -= 20;
			}

			uiElements.add(new Button(5, ay, 90, 10, "Cancel", new LabelActionHandler() {
				@Override
				public void actionHappened(Label label, float delta) {
					label.visible = buildClazz != null;
				}
			}, new LabelActionHandler() {
				@Override
				public void actionHappened(Label label, float delta) {
					initBuild();
				}
			}));
		}

		@Override
		public void update(float delta, int mouseX, int mouseY, boolean[] mouseButtons) {
			hoverBuildClazz = null;
			super.update(delta, mouseX, mouseY, mouseButtons);
		}
	}

	private class BuildInfoWindow extends Window {

		private Class<? extends Module> lastBuildModule;

		public BuildInfoWindow() {
			super(wWidth - 250, 0, 70, 0);
			visible = false;
			lastBuildModule = null;
		}

		@Override
		public void init() {}

		@Override
		public void update(float delta, int mouseX, int mouseY, boolean[] mouseButtons) {
			super.update(delta, mouseX, mouseY, mouseButtons);

			if (lastBuildModule != hoverBuildClazz) {
				lastBuildModule = hoverBuildClazz;
				uiElements.clear();

				if (lastBuildModule == null) {
					visible = false;
				} else {
					visible = true;
					final Map<Resource, Float> moduleBuildCost = ModuleFactory.getModuleBuildCost(lastBuildModule);
					int count = moduleBuildCost.size();
					height = count * 15;
					y = wHeight - 100 - height;
					float ay = height - 15;
					for (final Resource resource : Resource.values()) {
						if (moduleBuildCost.containsKey(resource)) {
							uiElements.add(new Label(25, ay, 40, 10, leftPad("" + moduleBuildCost.get(resource).intValue(), 5), new LabelActionHandler() {
								@Override
								public void actionHappened(Label label, float delta) {
									label.color = SpaceStation.get().getResource(resource) < moduleBuildCost.get(resource) ? Color.FIREBRICK : Color.BLACK;
								}
							}));
							uiElements.add(new Label(5, ay - 2, 10, 10, resource.icon, IOFont.icons, Color.WHITE, null));
							ay -= 15;
						}
					}
				}
			}
		}
	}

	private class ResourceInfoWindow extends Window {

		public ResourceInfoWindow() {
			super(0, wHeight - 30, 0, 20);
		}

		@Override
		public void init() {
			width = 90 * Resource.values().length;
			x = (wWidth - width) / 2;

			float ax = 5;
			for (final Resource resource : Resource.values()) {
				uiElements.add(new Label(ax + 20, 2, 70, 10, "", new LabelActionHandler() {
					@Override
					public void actionHappened(Label label, float delta) {
						label.text = ((int) SpaceStation.get().getResource(resource))
								+ "/" + ((int) SpaceStation.get().getResourceMax(resource));
						label.color = SpaceStation.get().getResource(resource) < 0.5f ? Color.FIREBRICK : Color.BLACK;
					}
				}));
				uiElements.add(new Label(ax, 0, 10, 10, resource.icon, IOFont.icons, Color.WHITE, null));
				ax += 90;
			}
		}
	}

	private class MessageWindow extends Window {

		private String text;

		public MessageWindow(String text) {
			super(200, 150, wWidth - 400, wHeight - 300, true);
			this.text = text;
			init();
		}

		@Override
		public void init() {
			uiElements.add(new Label(10, height - 25, width - 50, 10, text, null));
			uiElements.add(new Button(width - 50, 10, 20, 10, "Ok", null, new LabelActionHandler() {
				@Override
				public void actionHappened(Label label, float delta) {
					paused = false;
					windows95.remove(MessageWindow.this);
				}
			}));
		}
	}
}
